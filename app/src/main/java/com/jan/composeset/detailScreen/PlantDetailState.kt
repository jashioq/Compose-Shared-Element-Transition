package com.jan.composeset.detailScreen

import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jan.composeset.AnimationConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Stable
class PlantDetailState {
    var dragEnabled by mutableStateOf(false)
        private set

    var offsetY by mutableFloatStateOf(0f)
        private set

    var boxDragSpeed by mutableFloatStateOf(AnimationConfig.DEFAULT_DRAG_SPEED)
        private set

    var visibilityTarget by mutableFloatStateOf(0F)
        private set

    var boxZIndex by mutableFloatStateOf(0F)
        private set

    val draggableState = DraggableState { delta ->
        offsetY = (offsetY + delta).coerceIn(
            AnimationConfig.DRAG_OFFSET_MIN,
            AnimationConfig.DRAG_OFFSET_MAX
        )
    }

    fun startEnterAnimation() {
        visibilityTarget = 1F
        CoroutineScope(Dispatchers.Main).launch {
            delay(AnimationConfig.CONTENT_FADE_MS.milliseconds)
            dragEnabled = true
            boxZIndex = 1F
        }
    }

    fun startExitAnimation(onComplete: () -> Unit) {
        dragEnabled = false
        visibilityTarget = 0F
        boxDragSpeed = AnimationConfig.CONTENT_FADE_MS.toFloat()
        offsetY = 0F
        CoroutineScope(Dispatchers.Main).launch {
            delay(AnimationConfig.CONTENT_FADE_MS.milliseconds)
            boxZIndex = 0F
            onComplete()
        }
    }

    fun disableDrag() {
        dragEnabled = false
    }

    fun resetForTransition() {
        visibilityTarget = 0F
        dragEnabled = false
    }
}

@Composable
fun rememberPlantDetailState(): PlantDetailState {
    return remember { PlantDetailState() }
}
