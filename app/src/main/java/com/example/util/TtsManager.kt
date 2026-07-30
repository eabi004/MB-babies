package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import com.example.model.Language
import java.util.Locale

class TtsManager(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.language = Locale.ENGLISH
        } else {
            Log.e("TtsManager", "TTS Initialization failed")
        }
    }

    fun speak(text: String, language: Language) {
        if (!isInitialized || tts == null) return

        val locale = when (language) {
            Language.ENGLISH -> Locale.ENGLISH
            Language.TELUGU -> Locale("te", "IN")
            Language.HINDI -> Locale("hi", "IN")
        }

        val result = tts?.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Fallback to English phonetics/text if specific language voice pack is missing
            tts?.language = Locale.ENGLISH
        }

        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "AksharaTtsId")
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
