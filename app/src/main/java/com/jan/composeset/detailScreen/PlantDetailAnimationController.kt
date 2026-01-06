package com.jan.composeset.detailScreen

import com.jan.composeset.AnimationConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class PlantDetailAnimationController(
    private val onNavigateBack: () -> Unit
) {
    private val actionChannel = Channel<AnimationAction>(Channel.BUFFERED)
    private val _animationState = MutableStateFlow(AnimationState())
    val animationState: StateFlow<AnimationState> = _animationState.asStateFlow()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    init {
        startActionProcessor()
    }

    private fun startActionProcessor() {
        scope.launch {
            for (action in actionChannel) {
                processAction(action)
            }
        }
    }

    private suspend fun processAction(action: AnimationAction) {
        when (action) {
            is AnimationAction.AnimateEntry -> handleEntryAnimation()
            is AnimationAction.AnimateExit -> handleExitAnimation(action.onComplete)
            is AnimationAction.UpdateOffsetAdjustment -> handleOffsetAdjustmentUpdate(action.adjustment)
            is AnimationAction.UpdateDragOffset -> handleDragOffsetUpdate(action)
        }
    }

    private suspend fun handleEntryAnimation() {
        _animationState.update { it.copy(isAnimating = true) }

        // Wait for bounds transition to complete FIRST
        delay(AnimationConfig.BOUNDS_TRANSITION_MS.milliseconds)

        // Stage 1: Enable box wrap content (offset adjustment will be calculated when height updates come in)
        _animationState.update { it.copy(boxShouldWrapContent = true) }

        // Stage 2: Start visibility fade-in
        _animationState.update { it.copy(contentVisibilityTarget = 1f) }
        delay(AnimationConfig.CONTENT_FADE_MS.milliseconds)

        // Stage 3: Enable drag and increase z-index
        _animationState.update {
            it.copy(
                dragEnabled = true,
                boxZIndex = 1f
            )
        }

        _animationState.update { it.copy(isAnimating = false) }
    }

    private suspend fun handleExitAnimation(onComplete: () -> Unit) {
        _animationState.update { it.copy(isAnimating = true) }

        // Stage 1: Disable drag, reset visibility, animate offset to zero
        _animationState.update {
            it.copy(
                dragEnabled = false,
                contentVisibilityTarget = 0f,
                boxOffsetYTarget = 0f,
                boxOffsetAnimationSpeed = AnimationConfig.CONTENT_FADE_MS.toFloat()
            )
        }
        delay(AnimationConfig.CONTENT_FADE_MS.milliseconds)

        // Stage 2: Reset box wrap content AND offset adjustment in the SAME emission
        _animationState.update {
            it.copy(
                boxShouldWrapContent = false,
                boxZIndex = 0f,
                boxOffsetAnimationSpeed = AnimationConfig.DEFAULT_DRAG_SPEED,
                offsetAdjustment = 0f
            )
        }

        _animationState.update { it.copy(isAnimating = false) }

        onNavigateBack()
        onComplete()
    }

    private suspend fun handleOffsetAdjustmentUpdate(adjustment: Float) {
        _animationState.update { it.copy(offsetAdjustment = adjustment) }
    }

    private suspend fun handleDragOffsetUpdate(action: AnimationAction.UpdateDragOffset) {
        val newOffset = (_animationState.value.boxOffsetYTarget + action.delta).coerceIn(
            action.minOffset,
            action.maxOffset
        )

        _animationState.update {
            it.copy(
                boxOffsetYTarget = newOffset,
                boxOffsetAnimationSpeed = AnimationConfig.DEFAULT_DRAG_SPEED
            )
        }
    }

    fun animateEntry() {
        actionChannel.trySend(AnimationAction.AnimateEntry)
    }

    fun animateExit(onComplete: () -> Unit = {}) {
        actionChannel.trySend(AnimationAction.AnimateExit(onComplete))
    }

    fun updateOffsetAdjustment(adjustment: Float) {
        actionChannel.trySend(AnimationAction.UpdateOffsetAdjustment(adjustment))
    }

    fun updateDragOffset(delta: Float, minOffset: Float, maxOffset: Float) {
        actionChannel.trySend(AnimationAction.UpdateDragOffset(delta, minOffset, maxOffset))
    }

    fun dispose() {
        scope.cancel()
        actionChannel.close()
    }
}
