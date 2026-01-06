package com.jan.composeset.detailScreen

sealed class AnimationAction {
    object AnimateEntry : AnimationAction()
    data class AnimateExit(val onComplete: () -> Unit) : AnimationAction()
    data class UpdateOffsetAdjustment(val adjustment: Float) : AnimationAction()
    data class UpdateDragOffset(val delta: Float, val minOffset: Float, val maxOffset: Float) : AnimationAction()
}
