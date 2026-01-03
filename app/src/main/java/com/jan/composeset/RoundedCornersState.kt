package com.jan.composeset

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CornerRadiusState(
    initialProgress: Float = 0F,
) {
    var cornerRadius: Float by mutableStateOf(initialProgress)
        private set

    fun updateRadius(newRadius: Float) {
        cornerRadius = newRadius.coerceAtLeast(0F)
    }
}