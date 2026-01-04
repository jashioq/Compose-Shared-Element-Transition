package com.jan.composeset

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.size
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun SharedTransitionScope.PlantDetailScreen(
    plantId: Int,
    animatedVisibilityScope: AnimatedVisibilityScope,
    boundsTransform: BoundsTransform,
    cornerRadius: Dp,
    onBackClick: () -> Unit,
) {
    val plant = plants.firstOrNull { it.id == plantId } ?: return

    val contentAnimationMilliseconds = 500

    // Drag state for the box
    var dragEnabled by remember { mutableStateOf(false) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    val defaultBoxDragSpeed = 30F
    var boxDragSpeed by remember { mutableFloatStateOf(defaultBoxDragSpeed) }
    var boxOffset = animateFloatAsState(
        targetValue = offsetY,
        animationSpec = tween(durationMillis = boxDragSpeed.toInt(), easing = if(boxDragSpeed == defaultBoxDragSpeed) LinearEasing else FastOutSlowInEasing),
    )

    val draggableState = rememberDraggableState { delta ->
        offsetY = (offsetY + delta).coerceIn(-1000F, 0F)
    }

    var visibilityTarget by remember { mutableFloatStateOf(0F) }
    val contentVisibility by animateFloatAsState(
        targetValue = visibilityTarget,
        animationSpec = tween(durationMillis = contentAnimationMilliseconds),
        label = "contentVisibility"
    )

    var boxZIndex by remember { mutableFloatStateOf(0F) }

    LaunchedEffect(animatedVisibilityScope.transition.currentState) {
        // Reset states when transition starts
        visibilityTarget = 0F
        dragEnabled = false

        snapshotFlow { animatedVisibilityScope.transition.isRunning }
            .collect { isRunning ->
                if (!isRunning) {
                    visibilityTarget = 1F
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(contentAnimationMilliseconds.milliseconds)
                        dragEnabled = true
                        boxZIndex = 1F
                    }
                }
            }
    }

    fun backTransition(onBackClick: () -> Unit) {
        dragEnabled = false
        visibilityTarget = 0F
        boxDragSpeed = contentAnimationMilliseconds.toFloat()
        offsetY = 0F
        CoroutineScope(Dispatchers.Main).launch {
            delay(contentAnimationMilliseconds.milliseconds)
            boxZIndex = 0F
            onBackClick()
        }
    }

    BackHandler(onBack = {
        backTransition(onBackClick)
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Plant image
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
                    .offset { IntOffset(0, boxOffset.value.roundToInt()) }
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = "box-${plant.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = boundsTransform,
                    )
                    .let { modifier ->
                        if (dragEnabled) {
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
                            topStart = 32.dp,
                            topEnd = 32.dp,
                            bottomStart = cornerRadius,
                            bottomEnd = cornerRadius
                        )
                    )
                    .background(Color(0xFFdfe6d5))
                    .zIndex(boxZIndex),
            ) {
                // Content with reveal animation
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
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

                    Spacer(modifier = Modifier.height(8.dp))

                    // Scientific name
                    Text(
                        text = plant.scientificName,
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color.Gray,
                        modifier = Modifier
                            .sharedElement(
                                sharedContentState = rememberSharedContentState(key = "scientific-name-${plant.id}"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = boundsTransform,
                            ),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Details
                    Text(
                        text = plant.details,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.alpha(contentVisibility)
                    )
                }
            }
        }
        // Back button overlaying the image in top left corner
        IconButton(
            onClick = { backTransition(onBackClick) },
            modifier = Modifier
                .padding(16.dp)
                .alpha(contentVisibility)
                .background(Color(0x30000000), RoundedCornerShape(50))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}
