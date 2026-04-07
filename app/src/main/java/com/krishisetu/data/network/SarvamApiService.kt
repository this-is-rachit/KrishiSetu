package com.krishisetu.data.network

import okhttp3.MultipartBody
import retrofit2.http.*

interface SarvamApiService {

    // Speech to Text
    @Multipart
    @POST("speech-to-text")
    suspend fun transcribe(
        @Part file: MultipartBody.Part,
        @Part("model") model: okhttp3.RequestBody,
        @Part("language_code") languageCode: okhttp3.RequestBody
    ): SarvamSTTResponse

    // Translation
    @POST("translate")
    suspend fun translate(
        @Body request: SarvamTranslateRequest
    ): SarvamTranslateResponse

    // Text to Speech
    @POST("text-to-speech")
    suspend fun synthesize(
        @Body request: SarvamTTSRequest
    ): SarvamTTSResponse
}