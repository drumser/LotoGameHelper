package com.example.loto.game.service

import android.speech.tts.TextToSpeech

class SpeakerServiceImpl(private val TTS: TextToSpeech) : SpeakerService {
    override fun speak(text: String) {
        TTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}