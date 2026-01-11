package com.jan.composeset.detailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jan.composeset.AnimationConfig
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class PlantDetailViewModel : ViewModel() {
    private val actionChannel = Channel<AnimationAction>(Channel.BUFFERED)
    private val _animationState = MutableStateFlow(AnimationState())
    val animationState: StateFlow<AnimationState> = _animationState.asStateFlow()

    init {
        startActionProcessor()
    }

    private fun startActionProcessor() {
        viewModelScope.launch {
            for (action in actionChannel) {
                processAction(action)
            }
        }
    }

    private suspend fun processAction(action: AnimationAction) {
        when (action) {
            is AnimationAction.AnimateEntry -> handleEntryAnimation()
            is AnimationAction.AnimateExit -> handleExitAnimation(action.onComplete)
            is AnimationAction.UpdateDragOffset -> handleDragOffsetUpdate(action)
        }
    }

    /**
     * Orchestrates the 3-stage entry animation sequence for the plant detail screen.
     *
     * This demonstrates advanced animation coordination by combining SharedTransitionScope bounds
     * animation with custom content animations, using coroutine delays to stage the transitions.
     *
     * Animation Stages:
     * 1. Bounds Transition (0-1000ms): SharedTransitionScope animates shared elements
     * 2. Content Expansion (1000-1500ms): Box wraps content, height changes trigger offset adjustment
     * 3. Content Fade-in (1000-1500ms): Alpha animates 0f to 1f, background color lerps white to gray
     * 4. Enable Interactivity (1500ms+): Dragging enabled, z-index increased to overlay above list
     *
     * Key Techniques:
     * - Uses kotlinx.coroutines.delay to sequence animations
     * - StateFlow updates trigger reactive UI changes
     * - Offset adjustment auto-calculates when AnimationState.boxShouldWrapContent changes
     *
     * @see AnimationState.contentVisibilityTarget Controls fade-in alpha (0f to 1f)
     * @see AnimationState.boxShouldWrapContent Enables dynamic height measurement
     * @see AnimationConfig.BOUNDS_TRANSITION_MS Shared element transition duration
     * @see AnimationConfig.CONTENT_FADE_MS Content visibility animation duration
     */
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

    /**
     * Orchestrates the exit animation sequence, reversing the entry stages.
     *
     * Animation Stages:
     * 1. Disable Drag + Fade Out (0-500ms): Content alpha to 0f, offset to 0f, background color to white
     * 2. Reset Layout (500ms): Box collapses, offset adjustment cleared simultaneously
     * 3. Navigation: Triggers onComplete callback after animations complete
     *
     * Critical Detail: Stage 2 resets boxShouldWrapContent AND offsetAdjustment in the
     * SAME state emission to avoid visual flickering during the bounds transition back to list.
     *
     * @param onComplete Callback invoked after all animations finish (e.g., for navigation)
     */
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

        onComplete()
    }

    /**
     * Updates the drag offset while constraining movement within calculated bounds.
     *
     * The bounds are calculated dynamically based on content height changes:
     * - minOffset = -(expandedHeight - collapsedHeight) (can drag up to fully collapse)
     * - maxOffset = 0f (cannot drag down beyond natural position)
     *
     * Uses AnimationConfig.DEFAULT_DRAG_SPEED (30ms) for immediate, linear tracking
     * of finger movement during drag.
     */
    private fun handleDragOffsetUpdate(action: AnimationAction.UpdateDragOffset) {
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

    fun animateExit(onNavigateBack: () -> Unit) {
        actionChannel.trySend(AnimationAction.AnimateExit(onNavigateBack))
    }

    fun updateDragOffset(delta: Float, minOffset: Float, maxOffset: Float) {
        actionChannel.trySend(AnimationAction.UpdateDragOffset(delta, minOffset, maxOffset))
    }

    override fun onCleared() {
        super.onCleared()
        actionChannel.close()
    }
}
