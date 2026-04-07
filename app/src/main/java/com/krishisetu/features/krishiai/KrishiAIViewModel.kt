package com.krishisetu.features.krishiai

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishisetu.AppState
import com.krishisetu.BuildConfig
import com.krishisetu.data.network.AudioPlayer
import com.krishisetu.data.network.SarvamRepository
import com.krishisetu.model.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

data class KrishiAIState(
    val messages: List<ChatMessage> = emptyList(),
    val isTyping: Boolean           = false,
    val isListening: Boolean        = false,
    val isSpeaking: Boolean         = false,
    val selectedLanguage: String    = AppState.selectedLanguageCode,
    val error: String?              = null
)

@HiltViewModel
class KrishiAIViewModel @Inject constructor(
    private val sarvamRepo: SarvamRepository,
    private val audioPlayer: AudioPlayer,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(KrishiAIState())
    val state: StateFlow<KrishiAIState> = _state.asStateFlow()

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS) // Increased timeout
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    init {
        Log.d("KrishiAI", "ViewModel Initialized. Selected Language: ${AppState.selectedLanguageCode}")
        initGreeting()
    }

    private fun initGreeting() {
        val lang = AppState.selectedLanguageCode
        val text = if (lang == "hi-IN") "नमस्ते! मैं KrishiSetu AI हूं। मंडी भाव, फसल उपचार या सरकारी योजनाओं की सटीक जानकारी लें।"
        else "Hello! I'm KrishiSetu AI. Get precise info on mandi rates, crop diseases, and govt schemes."
        _state.value = _state.value.copy(messages = listOf(ChatMessage("0", text, false, currentTime())), selectedLanguage = lang)
    }

    // ── Gemini 2.5 Flash-Lite — Professional Prompt ───────────────────────────
    private suspend fun callGemini(userPrompt: String): String =
        withContext(Dispatchers.IO) {
            try {
                val key = BuildConfig.GEMINI_API_KEY
                val model = "gemini-2.5-flash-lite" // Ensuring 2026 compatibility

                Log.d("KrishiAI", "Calling Gemini API with model: $model")

                val instructions = """
                    You are KrishiSetu — India's smartest agricultural AI assistant. Answer like an expert who gives DIRECT, SPECIFIC, ACTIONABLE advice. No fluff.

                    OUTPUT FORMAT (strict):
                    - 4-5 bullets maximum
                    - Use a numbered list (1. 2. 3.). 
                    - Strictly format every response as a vertical list ensuring a double break follows every point to prevent paragraph merging.
                    - No fillers.
                    - No bold, no headers, no markdown
                    - Total response under 120 words
                    - No emojis
                    - Respond only in English

                    ANSWER RULES by query type:

                    MANDI PRICES → Give ₹/quintal for 3-4 specific mandis. State trend (rising/falling). Give sell/hold advice.
                    CROP DISEASE → Name the disease. Give 1 organic + 1 chemical treatment with exact dosage. Say when to see results.
                    GOVT SCHEME → Name exact scheme. State exact benefit amount. Give application method/portal.
                    EXPORT → State destination country + price in USD/MT. Name required certificates (APEDA, IEC, FSSAI).
                    PROCESSING → State value multiplier (e.g. 4x). Name specific product. Mention subsidy if available.
                    OFF-TOPIC → Reply: "I can only help with farming — mandi prices, crop care, schemes, and export."

                    Today's date context: April 2026, India, Kharif season approaching.
                    Query: $userPrompt
                """.trimIndent()

                val escaped = instructions.replace("\"", "\\\"").replace("\n", "\\n")

                // Using 'role' field for better compatibility
                val requestBody = """
                    {
                      "contents": [
                        {
                          "role": "user",
                          "parts": [{"text": "$escaped"}]
                        }
                      ],
                      "generationConfig": {
                        "temperature": 0.2,
                        "maxOutputTokens": 800
                      }
                    }
                """.trimIndent()

                // Switched to v1beta for better "Flash-Lite" support
                val request = Request.Builder()
                    .url("https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$key")
                    .addHeader("Content-Type", "application/json")
                    .post(requestBody.toRequestBody("application/json".toMediaType()))
                    .build()

                val response = httpClient.newCall(request).execute()
                val respStr = response.body?.string() ?: ""

                Log.d("KrishiAI", "Gemini HTTP Code: ${response.code}")

                if (response.isSuccessful) {
                    val extracted = respStr.substringAfter("\"text\": \"", "")
                        .substringBefore("\"")
                        .replace("\\n", "\n")
                        .trim()
                    Log.d("KrishiAI", "Gemini success. Extracted: ${extracted.take(50)}...")
                    extracted
                } else {
                    Log.e("KrishiAI", "Gemini Error: $respStr")
                    ""
                }
            } catch (e: Exception) {
                Log.e("KrishiAI", "Gemini Exception: ${e.message}")
                ""
            }
        }

    private suspend fun getAIResponse(query: String, userLangCode: String): String {
        val isEnglish = userLangCode == "en-IN"
        val englishQuery = if (!isEnglish) sarvamRepo.translate(query, userLangCode, "en-IN").getOrElse { query } else query

        val response = callGemini(englishQuery)
        if (response.isBlank()) return getFallbackResponse(englishQuery)

        // IF USER WANTS ENGLISH: No translation needed, just return.
        if (isEnglish) return response

        // IF USER WANTS REGIONAL: Solve the merging issue here
        return try {
            // 1. Split the Gemini response into individual bullets
            val points = response.split("\n\n").filter { it.isNotBlank() }

            // 2. Translate each bullet separately so Sarvam CANNOT merge them
            val translatedPoints = points.map { point ->
                sarvamRepo.translate(point, "en-IN", userLangCode).getOrElse { point }
            }

            // 3. Join them back with double newlines for the UI
            translatedPoints.joinToString(separator = "\n\n")
        } catch (e: Exception) {
            response // Fallback to English if translation fails
        }
    }

    // ── Rest of the Logic (Standard) ──────────────────────────────────────────

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        Log.d("KrishiAI", "User sent message: $text")
        addMessage(ChatMessage(UUID.randomUUID().toString(), text, true, currentTime()))
        _state.value = _state.value.copy(isTyping = true)

        viewModelScope.launch {
            val response = getAIResponse(text, _state.value.selectedLanguage)
            addMessage(ChatMessage(UUID.randomUUID().toString(), response, false, currentTime()))
            _state.value = _state.value.copy(isTyping = false)
        }
    }

    fun startListening() { _state.value = _state.value.copy(isListening = true); sarvamRepo.startRecording() }

    fun stopListening() {
        viewModelScope.launch {
            val audio = sarvamRepo.stopRecording()
            _state.value = _state.value.copy(isListening = false, isTyping = true)
            audio?.let {
                sarvamRepo.transcribe(it, _state.value.selectedLanguage).onSuccess { transcript ->
                    addMessage(ChatMessage(UUID.randomUUID().toString(), transcript, true, currentTime(), true))
                    val resp = getAIResponse(transcript, _state.value.selectedLanguage)
                    addMessage(ChatMessage(UUID.randomUUID().toString(), resp, false, currentTime()))
                }
                it.delete()
            }
            _state.value = _state.value.copy(isTyping = false)
        }
    }

    fun speakMessage(text: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSpeaking = true)
            sarvamRepo.synthesize(text.take(450), _state.value.selectedLanguage).onSuccess {
                audioPlayer.playBase64Audio(it, context.cacheDir)
            }
            _state.value = _state.value.copy(isSpeaking = false)
        }
    }

    fun setLanguage(code: String) {
        Log.d("KrishiAI", "Language changed to: $code")
        _state.value = _state.value.copy(selectedLanguage = code)
    }

    fun clearChat() { initGreeting() }

    private fun addMessage(msg: ChatMessage) {
        _state.value = _state.value.copy(messages = _state.value.messages + msg)
    }

    private fun currentTime() = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

    private fun getFallbackResponse(query: String): String {
        val q = query.lowercase()
        return when {
            q.containsAny("beej", "seed", "scheme") -> "🌱 UP Seed Subsidy: 50% discount on seeds.\n\n• 💰 PM-Kisan: ₹6,000 yearly benefit."
            q.containsAny("price", "bhav") -> "📍 Mandi Prices: ₹1,800 - ₹2,200 per quintal."
            else -> "🤖 I am KrishiSetu AI. Ask me about Mandi prices, Crop care, or Govt Schemes."
        }
    }
}

private fun String.containsAny(vararg keywords: String): Boolean = keywords.any { this.contains(it, ignoreCase = true) }