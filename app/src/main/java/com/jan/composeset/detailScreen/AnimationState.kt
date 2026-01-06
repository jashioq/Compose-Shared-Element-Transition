package com.jan.composeset.detailScreen

data class AnimationState(
    val dragEnabled: Boolean = false,
    val boxShouldWrapContent: Boolean = false,
    val boxZIndex: Float = 0f,
    val offsetAdjustment: Float = 0f,
    val isAnimating: Boolean = false,

    // Target values for UI animations
    val boxOffsetYTarget: Float = 0f,
    val contentVisibilityTarget: Float = 0f,
    val boxOffsetAnimationSpeed: Float = 30f
)
