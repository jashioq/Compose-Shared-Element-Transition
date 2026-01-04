package com.jan.composeset.detailScreen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.jan.composeset.Dimensions
import com.jan.composeset.Plant
import com.jan.composeset.ui.theme.PlantCardBackground
import com.jan.composeset.ui.theme.ScientificNameGray
import kotlin.math.roundToInt

@Composable
fun SharedTransitionScope.PlantDetailImage(
    plant: Plant,
    cornerRadius: Dp,
    animatedVisibilityScope: AnimatedVisibilityScope,
    boundsTransform: BoundsTransform
) {
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
    }
}

@Composable
fun SharedTransitionScope.PlantDetailBox(
    plant: Plant,
    state: PlantDetailState,
    boxOffset: Float,
    cornerRadius: Dp,
    contentVisibility: Float,
    animatedVisibilityScope: AnimatedVisibilityScope,
    boundsTransform: BoundsTransform
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .offset { IntOffset(0, boxOffset.roundToInt()) }
            .sharedElement(
                sharedContentState = rememberSharedContentState(key = "box-${plant.id}"),
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = boundsTransform,
            )
            .let { modifier ->
                if (state.dragEnabled) {
                    modifier.draggable(
                        state = state.draggableState,
                        orientation = Orientation.Vertical
                    )
                } else {
                    modifier
                }
            }
            .clip(
                RoundedCornerShape(
                    topStart = Dimensions.CornerRadiusLarge,
                    topEnd = Dimensions.CornerRadiusLarge,
                    bottomStart = cornerRadius,
                    bottomEnd = cornerRadius
                )
            )
            .background(PlantCardBackground)
            .zIndex(state.boxZIndex),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.ContentPadding)
                .skipToLookaheadSize()
        ) {
            // Shared title
            Text(
                text = plant.name,
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = "name-${plant.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = boundsTransform,
                    ),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(Dimensions.MediumSpacing))

            // Scientific name
            Text(
                text = plant.scientificName,
                fontSize = 18.sp,
                fontStyle = FontStyle.Italic,
                color = ScientificNameGray,
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = "scientific-name-${plant.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = boundsTransform,
                    ),
            )

            Spacer(modifier = Modifier.height(Dimensions.LargeSpacing))

            // Details
            Text(
                text = plant.details,
                fontSize = 14.sp,
                color = ScientificNameGray,
                modifier = Modifier.alpha(contentVisibility)
            )
        }
    }
}

@Composable
fun PlantDetailBackButton(
    onClick: () -> Unit,
    alpha: Float
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .padding(Dimensions.ContentPadding)
            .alpha(alpha)
            .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(Dimensions.BackButtonSize))
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.Black,
            modifier = Modifier.fillMaxSize()
        )
    }
}
