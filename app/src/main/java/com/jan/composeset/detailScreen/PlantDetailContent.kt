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
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.util.fastRoundToInt
import com.jan.composeset.AnimationConfig
import com.jan.composeset.Dimensions
import com.jan.composeset.Plant
import com.jan.composeset.ui.theme.PlantCardBackground
import com.jan.composeset.ui.theme.ScientificNameGray
import kotlin.math.floor
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
    animationController: PlantDetailAnimationController,
    cornerRadius: Dp,
    animatedVisibilityScope: AnimatedVisibilityScope,
    boundsTransform: BoundsTransform
) {
    // Collect animation state
    val animationState by animationController.animationState.collectAsState()

    // Create internal state for height tracking
    var boxHeightBeforeDrag by remember { mutableFloatStateOf(0f) }
    var boxHeightAfterDrag by remember { mutableFloatStateOf(0f) }

    // Create draggable state that sends to controller
    val draggableState = remember {
        DraggableState { delta ->
            animationController.updateDragOffset(delta)
        }
    }

    // Animate box offset using target and speed from controller
    val boxOffset by androidx.compose.animation.core.animateFloatAsState(
        targetValue = animationState.boxOffsetYTarget,
        animationSpec = androidx.compose.animation.core.tween(
            durationMillis = animationState.boxOffsetAnimationSpeed.toInt(),
            easing = if (animationState.boxOffsetAnimationSpeed == AnimationConfig.DEFAULT_DRAG_SPEED)
                androidx.compose.animation.core.LinearEasing
            else
                androidx.compose.animation.core.FastOutSlowInEasing
        ),
        label = "boxOffset"
    )

    // Animate content visibility using target from controller
    val contentVisibility by androidx.compose.animation.core.animateFloatAsState(
        targetValue = animationState.contentVisibilityTarget,
        animationSpec = androidx.compose.animation.core.tween(
            durationMillis = AnimationConfig.CONTENT_FADE_MS
        ),
        label = "contentVisibility"
    )

    // Trigger entry animation on first composition
    LaunchedEffect(Unit) {
        animationController.animateEntry()
    }

    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    // Image uses aspectRatio(1f) on fillMaxWidth, so image height = screen width
    val imageHeight = screenWidth
    val boxAvailableHeight = (screenHeight - imageHeight).value

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .let { modifier ->
                if (animationState.boxShouldWrapContent) {
                    modifier.wrapContentHeight(unbounded = true)
                } else {
                    modifier.fillMaxSize()
                }
            }
            .defaultMinSize(minHeight = boxAvailableHeight.dp)
            .onSizeChanged { size ->
                if (animationState.boxShouldWrapContent) {
                    boxHeightAfterDrag = size.height.toFloat()
                    // Calculate and update offset adjustment
                    val adjustment = if (boxHeightAfterDrag > 0f && boxHeightBeforeDrag > 0f) {
                        floor(((boxHeightAfterDrag - boxHeightBeforeDrag) / 2).coerceAtLeast(0f))
                    } else {
                        0f
                    }
                    animationController.updateOffsetAdjustment(adjustment)
                } else {
                    boxHeightBeforeDrag = size.height.toFloat()
                }
            }
            .offset {
                IntOffset(0, (boxOffset + animationState.offsetAdjustment).roundToInt())
            }
            .sharedElement(
                sharedContentState = rememberSharedContentState(key = "box-${plant.id}"),
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = boundsTransform,
            )
            .let { modifier ->
                if (animationState.dragEnabled) {
                    modifier.draggable(
                        state = draggableState,
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
            .zIndex(animationState.boxZIndex),
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
