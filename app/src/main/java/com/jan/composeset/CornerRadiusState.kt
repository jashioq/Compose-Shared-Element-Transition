package com.jan.composeset

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CornerRadiusState(
    initialProgress: Float = 0F,
) {
    var cornerRadius: Float by mutableStateOf(initialProgress)
        private set

    var selectedPlantId: Int? by mutableStateOf(null)
        private set

    fun updateRadius(newRadius: Float, plantId: Int? = null) {
        cornerRadius = newRadius.coerceAtLeast(0F)
        selectedPlantId = plantId
    }
}
