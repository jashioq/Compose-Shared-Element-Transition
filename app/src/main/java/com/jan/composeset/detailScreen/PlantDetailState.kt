package com.jan.composeset.detailScreen

import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jan.composeset.AnimationConfig
import kotlin.math.floor

@Stable
class PlantDetailState(
    private val animationController: PlantDetailAnimationController
) {
    var offsetY by mutableFloatStateOf(0f)
        internal set

    var boxDragSpeed by mutableFloatStateOf(AnimationConfig.DEFAULT_DRAG_SPEED)
        internal set

    var boxHeightBeforeDrag by mutableFloatStateOf(0f)
    var boxHeightAfterDrag by mutableFloatStateOf(0f)
    var boxAvailableHeight by mutableFloatStateOf(0f)

    val draggableState = DraggableState { delta ->
        offsetY = (offsetY + delta).coerceIn(
            AnimationConfig.DRAG_OFFSET_MIN,
            AnimationConfig.DRAG_OFFSET_MAX
        )
    }

    fun calculateAndUpdateOffsetAdjustment() {
        val adjustment = if (boxHeightAfterDrag > 0f && boxHeightBeforeDrag > 0f) {
            floor(((boxHeightAfterDrag - boxHeightBeforeDrag) / 2).coerceAtLeast(0f))
        } else {
            0f
        }
        animationController.updateOffsetAdjustment(adjustment)
    }

    fun resetForTransition() {
        offsetY = 0f
        boxDragSpeed = AnimationConfig.DEFAULT_DRAG_SPEED
    }
}

@Composable
fun rememberPlantDetailState(
    animationController: PlantDetailAnimationController
): PlantDetailState {
    return remember(animationController) {
        PlantDetailState(animationController)
    }
}
