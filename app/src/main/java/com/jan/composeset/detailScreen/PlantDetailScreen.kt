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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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

    // Create animation controller
    val animationController = remember {
        PlantDetailAnimationController(onNavigateBack = onBackClick)
    }

    // Collect animation state
    val animationState by animationController.animationState.collectAsState()

    // Animate back button alpha using same target as content visibility
    val backButtonAlpha by animateFloatAsState(
        targetValue = animationState.contentVisibilityTarget,
        animationSpec = tween(
            durationMillis = AnimationConfig.CONTENT_FADE_MS
        ),
        label = "backButtonAlpha"
    )

    // Dispose controller when leaving composition
    DisposableEffect(Unit) {
        onDispose {
            animationController.dispose()
        }
    }

    BackHandler(onBack = {
        animationController.animateExit()
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
                animationController = animationController,
                cornerRadius = cornerRadius,
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = boundsTransform
            )
        }

        PlantDetailBackButton(
            onClick = { animationController.animateExit() },
            alpha = backButtonAlpha
        )
    }
}
