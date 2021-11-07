package com.example.loto.game

import com.example.loto.service.SpeakerService
import kotlin.concurrent.fixedRateTimer

class LotoGame(
    private val speaker: SpeakerService,
    private var timerPeriod: Long = 6000L
) {
    private var isAppStarted = false
    private var isAppPaused = false
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

        fixedRateTimer("timer", false, 0L, timerPeriod) {
            if (isAppPaused) {
                return@fixedRateTimer
            }

            if (kegs.isEmpty() || !isAppStarted) {
                isAppPaused = false
                isAppStarted = false
                resetGame()
                onAppFinished()
                this.cancel()
            } else {
                val randomDigit = kegs.random()
                kegs.remove(randomDigit)
                speaker.speak(randomDigit.toString());
                onNewKeg(randomDigit)
            }
        }
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

    fun resetGame() {
        isAppStarted = false
        isAppPaused = false
        kegs = generateKegs()
        onAppFinished()
    }
}