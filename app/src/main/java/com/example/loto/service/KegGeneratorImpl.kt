package com.example.loto.service

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.loto.R

class KegGeneratorImpl(
    private val context: Context
) : KegGenerator {
    override fun generate(digit: Int): TextView {
        val digitView = TextView(context)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.rightMargin = 10
        layoutParams.bottomMargin = 10
        digitView.layoutParams = layoutParams
        digitView.width = 80
        digitView.height = 80
        digitView.setBackgroundResource(R.drawable.white_circle)
        digitView.gravity = Gravity.CENTER
        digitView.text = digit.toString()
        digitView.textAlignment = View.TEXT_ALIGNMENT_CENTER

        return digitView
    }
}