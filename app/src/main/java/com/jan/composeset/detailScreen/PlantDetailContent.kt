package com.jan.composeset.detailScreen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jan.composeset.AnimationConfig
import com.jan.composeset.Dimensions
import com.jan.composeset.Plant
import com.jan.composeset.detailScreen.components.PlantDetailBackButton
import com.jan.composeset.detailScreen.components.PlantDetailBox
import com.jan.composeset.detailScreen.components.PlantDetailImage
import com.jan.composeset.ui.theme.DetailBackground
import com.jan.composeset.ui.theme.InitialDetailBackground

@Composable
fun SharedTransitionScope.PlantDetailContent(
    plant: Plant,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: PlantDetailViewModel,
    cornerRadius: Dp
) {
    val animationState by viewModel.animationState.collectAsState()

    val boundsTransform = BoundsTransform { _, _ ->
        androidx.compose.animation.core.tween(durationMillis = AnimationConfig.BOUNDS_TRANSITION_MS)
    }

    val backButtonAlpha by androidx.compose.animation.core.animateFloatAsState(
        targetValue = animationState.contentVisibilityTarget,
        animationSpec = androidx.compose.animation.core.tween(
            durationMillis = AnimationConfig.CONTENT_FADE_MS
        ),
        label = "backButtonAlpha"
    )

    // Animate background color from white to gray during content fade
    val backgroundColor by androidx.compose.animation.animateColorAsState(
        targetValue = androidx.compose.ui.graphics.lerp(
            InitialDetailBackground,
            DetailBackground,
            animationState.contentVisibilityTarget
        ),
        animationSpec = androidx.compose.animation.core.tween(
            durationMillis = AnimationConfig.CONTENT_FADE_MS
        ),
        label = "backgroundColor"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column {
            PlantDetailImage(
                plant = plant,
                cornerRadius = cornerRadius,
                animatedVisibilityScope = animatedVisibilityScope,
                viewModel = viewModel,
                boundsTransform = boundsTransform
            )

            PlantDetailBox(
                plant = plant,
                viewModel = viewModel,
                cornerRadius = cornerRadius,
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = boundsTransform
            )
        }

        PlantDetailBackButton(
            onClick = { viewModel.animateExit() },
            alpha = backButtonAlpha,
            modifier = Modifier.align(Alignment.TopStart)
        )
    }
}
