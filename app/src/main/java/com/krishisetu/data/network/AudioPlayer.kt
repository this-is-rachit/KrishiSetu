package com.krishisetu.data.network

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioPlayer @Inject constructor() {
    private var mediaPlayer: MediaPlayer? = null

    suspend fun playBase64Audio(base64Audio: String, cacheDir: File) =
        withContext(Dispatchers.IO) {
            try {
                // Decode base64 to file
                val audioBytes = Base64.decode(base64Audio, Base64.DEFAULT)
                val tempFile = File(cacheDir, "tts_output_${System.currentTimeMillis()}.wav")
                FileOutputStream(tempFile).use { it.write(audioBytes) }

                withContext(Dispatchers.Main) {
                    mediaPlayer?.release()
                    mediaPlayer = MediaPlayer().apply {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                        )
                        setDataSource(tempFile.absolutePath)
                        prepare()
                        start()
                        setOnCompletionListener {
                            it.release()
                            tempFile.delete()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}