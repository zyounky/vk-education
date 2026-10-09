package com.example.vkeducation

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

internal fun View.applySystemBarInsets() {
    val left = paddingLeft
    val top = paddingTop
    val right = paddingRight
    val bottom = paddingBottom
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        val keyboard = insets.getInsets(WindowInsetsCompat.Type.ime())
        view.updatePadding(
            left = left + bars.left,
            top = top + bars.top,
            right = right + bars.right,
            bottom = bottom + maxOf(bars.bottom, keyboard.bottom),
        )
        insets
    }
    ViewCompat.requestApplyInsets(this)
}
