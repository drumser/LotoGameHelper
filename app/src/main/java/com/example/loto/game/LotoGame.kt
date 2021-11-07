package com.example.loto.game

import com.example.loto.game.service.SpeakerService
import java.util.*
import kotlin.concurrent.fixedRateTimer

class LotoGame(
    private val speaker: SpeakerService,
    private var timerPeriod: Long = 6000L
) {
    private var isAppStarted = false
    private var isAppPaused = false
    private var timer: Timer? = null
    var onAppStarted: () -> Unit = {}
    var onAppFinished: () -> Unit = {}
    var onNewKeg: (digit: Int) -> Unit = {}

    private var kegs: MutableList<Int> = generateKegs()

    fun isPaused() = isAppPaused
    fun isAppStarted() = isAppStarted

    fun startGame() {
        isAppStarted = true
        isAppPaused = false
        onAppStarted()

        timer = fixedRateTimer("timer", false, 0L, timerPeriod) {
            if (isAppPaused) {
                return@fixedRateTimer
            }

            if (kegs.isEmpty() || !isAppStarted) {
                stopGame()
            } else {
                val randomDigit = kegs.random()
                kegs.remove(randomDigit)
                speaker.speak(randomDigit.toString());
                onNewKeg(randomDigit)
            }
        }
    }

    fun stopGame() {
        isAppStarted = false
        isAppPaused = false
        timer?.cancel()
        kegs = generateKegs()
        onAppFinished()
    }

    fun togglePlay() {
        if (!isAppStarted) {
            return
        }

        if (isAppPaused) {
            play()
        } else {
            pause()
        }
    }

    private fun play() {
        if (!isAppStarted) {
            return
        }

        isAppPaused = false
    }

    private fun pause() {
        if (!isAppStarted) {
            return
        }

        isAppPaused = true
    }

    private fun generateKegs() = (1..90).toMutableList().apply {
        this.shuffle()
    }
}