package com.example.loto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.widget.TextView
import com.example.loto.game.LotoGame
import com.example.loto.service.KegGeneratorImpl
import com.example.loto.service.SpeakerServiceImpl
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.button.MaterialButton
import java.util.*

class MainActivity : AppCompatActivity(), OnInitListener {

    private lateinit var TTS: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playButton = findViewById<MaterialButton>(R.id.button_play)
        val resetButton = findViewById<MaterialButton>(R.id.button_reset)
        val currentKegView = findViewById<TextView>(R.id.text_current_keg)
        val kegContainer = findViewById<FlexboxLayout>(R.id.flexbox_keg_container)

        val kegGenerator = KegGeneratorImpl(applicationContext)
        TTS = TextToSpeech(this, this)
        val speaker = SpeakerServiceImpl(TTS)

        val lotoGame = LotoGame(
            speaker,
            20L
        )

        lotoGame.onNewKeg = {
            runOnUiThread {
                currentKegView.text = it.toString()
                kegContainer.addView(kegGenerator.generate(it))
            }
        }

        lotoGame.onAppFinished = {
            runOnUiThread {
                kegContainer.removeAllViews()
                currentKegView.text = ""
                playButton.setIconResource(R.drawable.ic_media_play)
            }
        }

        playButton.setOnClickListener {
            runOnUiThread {
                when (lotoGame.isAppStarted()) {
                    false -> lotoGame.startGame()
                    true -> lotoGame.togglePlay()
                }

                if (!lotoGame.isPaused()) {
                    playButton.setIconResource(R.drawable.ic_media_pause)
                } else {
                    playButton.setIconResource(R.drawable.ic_media_play)
                }
            }
        }
        resetButton.setOnClickListener {
            lotoGame.resetGame()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        TTS.stop()
        TTS.shutdown()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            if (TTS.isLanguageAvailable(Locale(Locale.getDefault().getLanguage()))
                == TextToSpeech.LANG_AVAILABLE
            ) {
                TTS.setLanguage(Locale(Locale.getDefault().getLanguage()));
            } else {
                TTS.setLanguage(Locale.US);
            }
            TTS.setPitch(1.3f);
            TTS.setSpeechRate(0.7f);
        }
    }
}