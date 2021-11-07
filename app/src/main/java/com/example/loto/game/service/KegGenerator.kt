package com.example.loto.game.service

import android.widget.TextView

interface KegGenerator {
    fun generate(digit: Int): TextView
}