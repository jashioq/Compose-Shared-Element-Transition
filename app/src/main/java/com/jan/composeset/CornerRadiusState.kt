package com.jan.composeset

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Shared state holder for animating corner radius during navigation transitions.
 *
 * This pattern can animate any property: colors, sizes, positions, etc.
 *
 * @property cornerRadius Current corner radius value (animated by Navigation.kt)
 * @property selectedPlantId ID of the plant being transitioned (used for conditional styling)
 */
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
