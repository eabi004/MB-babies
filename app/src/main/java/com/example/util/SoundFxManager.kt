package com.example.util

import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log

class SoundFxManager {
    private var toneGenerator: ToneGenerator? = try {
        ToneGenerator(AudioManager.STREAM_MUSIC, 80)
    } catch (e: Exception) {
        Log.e("SoundFxManager", "ToneGenerator init failed", e)
        null
    }

    fun playClickSound() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 40)
        } catch (e: Exception) {
            Log.e("SoundFxManager", "playClickSound error", e)
        }
    }

    fun playCorrectSound() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 250)
        } catch (e: Exception) {
            Log.e("SoundFxManager", "playCorrectSound error", e)
        }
    }

    fun playIncorrectSound() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 200)
        } catch (e: Exception) {
            Log.e("SoundFxManager", "playIncorrectSound error", e)
        }
    }

    fun playHintSound() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 120)
        } catch (e: Exception) {
            Log.e("SoundFxManager", "playHintSound error", e)
        }
    }

    fun release() {
        toneGenerator?.release()
        toneGenerator = null
    }
}
