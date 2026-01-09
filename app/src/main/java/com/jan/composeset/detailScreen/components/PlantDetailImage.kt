package com.jan.composeset.detailScreen.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jan.composeset.AnimationConfig
import com.jan.composeset.Plant
import com.jan.composeset.detailScreen.PlantDetailViewModel
import com.jan.composeset.ui.theme.DetailBackground

@Composable
fun SharedTransitionScope.PlantDetailImage(
    plant: Plant,
    cornerRadius: Dp,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: PlantDetailViewModel,
    boundsTransform: BoundsTransform
) {
    val animationState by viewModel.animationState.collectAsState()
    val contentVisibility by androidx.compose.animation.core.animateFloatAsState(
        targetValue = animationState.contentVisibilityTarget,
        animationSpec = androidx.compose.animation.core.tween(
            durationMillis = AnimationConfig.CONTENT_FADE_MS
        ),
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .sharedElement(
                sharedContentState = rememberSharedContentState(key = "image-${plant.id}"),
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = boundsTransform
            )
            .clip(RoundedCornerShape(cornerRadius))
    ) {
        Image(
            painter = painterResource(id = plant.imageRes),
            contentDescription = plant.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Black fading edge at the bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .align(androidx.compose.ui.Alignment.BottomCenter)
                .alpha(contentVisibility)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            DetailBackground,
                        )
                    )
                )
        )
    }
}
