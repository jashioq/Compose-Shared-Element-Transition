package com.jan.composeset.detailScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.jan.composeset.AnimationConfig
import com.jan.composeset.plants
import com.jan.composeset.ui.theme.DetailBackground
import com.jan.composeset.ui.theme.PlantCardBackground

@Composable
fun SharedTransitionScope.PlantDetailScreen(
    plantId: Int,
    animatedVisibilityScope: AnimatedVisibilityScope,
    boundsTransform: BoundsTransform,
    cornerRadius: Dp,
    onBackClick: () -> Unit,
) {
    val plant = plants.firstOrNull { it.id == plantId } ?: return
    val state = rememberPlantDetailState()

    val boxOffset by animateFloatAsState(
        targetValue = state.offsetY,
        animationSpec = tween(
            durationMillis = state.boxDragSpeed.toInt(),
            easing = if (state.boxDragSpeed == AnimationConfig.DEFAULT_DRAG_SPEED)
                LinearEasing else FastOutSlowInEasing
        ),
        label = "boxOffset"
    )

    val contentVisibility by animateFloatAsState(
        targetValue = state.visibilityTarget,
        animationSpec = tween(durationMillis = AnimationConfig.CONTENT_FADE_MS),
        label = "contentVisibility"
    )

    LaunchedEffect(animatedVisibilityScope.transition.currentState) {
        state.resetForTransition()

        snapshotFlow { animatedVisibilityScope.transition.isRunning }
            .collect { isRunning ->
                if (!isRunning) {
                    state.startEnterAnimation()
                }
            }
    }

    BackHandler(onBack = {
        state.startExitAnimation(onBackClick)
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DetailBackground)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            PlantDetailImage(
                plant = plant,
                cornerRadius = cornerRadius,
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = boundsTransform
            )

            PlantDetailBox(
                plant = plant,
                state = state,
                boxOffset = boxOffset,
                cornerRadius = cornerRadius,
                contentVisibility = contentVisibility,
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = boundsTransform
            )
        }

        PlantDetailBackButton(
            onClick = { state.startExitAnimation(onBackClick) },
            alpha = contentVisibility
        )
    }
}
