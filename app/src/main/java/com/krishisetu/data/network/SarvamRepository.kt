package com.krishisetu.data.network

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import com.krishisetu.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SarvamRepository @Inject constructor(
    private val api: SarvamApiService,
    @ApplicationContext private val context: Context
) {
    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null
    var isRecording = false
        private set

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    fun startRecording() {
        try {
            audioFile = File(
                context.cacheDir,
                "krishi_voice_${System.currentTimeMillis()}.mp3"
            )
            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioSamplingRate(16000)
                setAudioEncodingBitRate(64000)
                setOutputFile(audioFile!!.absolutePath)
                prepare()
                start()
            }
            isRecording = true
        } catch (e: Exception) {
            e.printStackTrace()
            isRecording = false
        }
    }

    fun stopRecording(): File? {
        return try {
            mediaRecorder?.apply { stop(); release() }
            mediaRecorder = null
            isRecording = false
            audioFile
        } catch (e: Exception) {
            e.printStackTrace()
            isRecording = false
            null
        }
    }

    // ── STT via direct OkHttp ─────────────────────────────────
    suspend fun transcribe(
        audioFile: File,
        languageCode: String = "hi-IN"
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val requestFile = audioFile.asRequestBody("audio/mpeg".toMediaTypeOrNull())

            val body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", audioFile.name, requestFile)
                .addFormDataPart("model", "saaras:v3")          // ← FIXED: was saaras:v2
                .addFormDataPart("language_code", languageCode)
                .addFormDataPart("with_timestamps", "false")
                .addFormDataPart("with_diarization", "false")
                .build()

            val request = okhttp3.Request.Builder()
                .url("https://api.sarvam.ai/speech-to-text")
                .addHeader("api-subscription-key", BuildConfig.SARVAM_API_KEY)
                .post(body)
                .build()

            val response = httpClient.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (response.isSuccessful) {
                val transcript = responseBody
                    .substringAfter("\"transcript\":\"", "")
                    .substringBefore("\"")
                Result.success(transcript)
            } else {
                Result.failure(Exception("STT Error ${response.code}: $responseBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ── Translation ───────────────────────────────────────────
    suspend fun translate(
        text: String,
        from: String,
        to: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = api.translate(
                SarvamTranslateRequest(
                    input              = text,
                    sourceLanguageCode = from,
                    targetLanguageCode = to
                )
            )
            Result.success(response.translatedText)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ── TTS ───────────────────────────────────────────────────
    suspend fun synthesize(
        text: String,
        languageCode: String = "hi-IN"
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = api.synthesize(
                SarvamTTSRequest(
                    inputs             = listOf(text),
                    targetLanguageCode = languageCode,
                    speaker            = "meera"
                )
            )
            Result.success(response.audios.firstOrNull() ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}