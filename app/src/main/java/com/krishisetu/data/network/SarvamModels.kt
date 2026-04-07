package com.krishisetu.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ── STT ───────────────────────────────────────────────────────
@Serializable
data class SarvamSTTResponse(
    val transcript: String = "",
    @SerialName("language_code")
    val languageCode: String = "hi-IN"
)

// ── Translation ───────────────────────────────────────────────
@Serializable
data class SarvamTranslateRequest(
    val input: String,
    @SerialName("source_language_code")
    val sourceLanguageCode: String,
    @SerialName("target_language_code")
    val targetLanguageCode: String,
    val model: String = "mayura:v1",
    val mode: String = "formal"
)

@Serializable
data class SarvamTranslateResponse(
    @SerialName("translated_text")
    val translatedText: String = ""
)

// ── TTS ───────────────────────────────────────────────────────
@Serializable
data class SarvamTTSRequest(
    val inputs: List<String>,
    @SerialName("target_language_code")
    val targetLanguageCode: String,
    val speaker: String = "meera",
    val model: String = "bulbul:v1"
)

@Serializable
data class SarvamTTSResponse(
    val audios: List<String> = emptyList()  // base64 encoded audio
)