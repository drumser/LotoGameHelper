package com.example.loto.service

import android.widget.TextView

interface KegGenerator {
    fun generate(digit: Int): TextView
}