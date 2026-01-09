package com.jan.composeset.detailScreen.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.jan.composeset.AnimationConfig
import com.jan.composeset.Dimensions
import com.jan.composeset.Plant
import com.jan.composeset.detailScreen.PlantDetailViewModel
import com.jan.composeset.ui.theme.LightGray
import com.jan.composeset.ui.theme.PlantCardBackground
import com.jan.composeset.ui.theme.ScientificNameGray
import kotlin.math.floor
import kotlin.math.roundToInt

@Composable
fun SharedTransitionScope.PlantDetailBox(
    plant: Plant,
    viewModel: PlantDetailViewModel,
    cornerRadius: Dp,
    animatedVisibilityScope: AnimatedVisibilityScope,
    boundsTransform: BoundsTransform
) {
    // Collect animation state
    val animationState by viewModel.animationState.collectAsState()

    // Create internal state for height tracking
    var boxHeightBeforeDrag by remember { mutableFloatStateOf(0f) }
    var boxHeightAfterDrag by remember { mutableFloatStateOf(0f) }

    // Helper function to calculate offset adjustment (like original implementation)
    fun getOffsetAdjustment(): Float {
        return if (boxHeightAfterDrag > 0f && boxHeightBeforeDrag > 0f) {
            floor(((boxHeightAfterDrag - boxHeightBeforeDrag) / 2).coerceAtLeast(0f))
        } else {
            0f
        }
    }

    // Create draggable state that sends to controller with dynamic limits
    val draggableState = remember {
        DraggableState { delta ->
            // Calculate drag limits based on current box size difference
            val maxDragDistance = if (boxHeightAfterDrag > 0f && boxHeightBeforeDrag > 0f) {
                -(boxHeightAfterDrag - boxHeightBeforeDrag)
            } else {
                0f
            }
            viewModel.updateDragOffset(delta, minOffset = maxDragDistance, maxOffset = 0f)
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
        viewModel.animateEntry()
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
                if (contentVisibility != 0f) {
                    modifier.wrapContentHeight(unbounded = true)
                } else {
                    modifier.fillMaxSize()
                }
            }
            .defaultMinSize(minHeight = boxAvailableHeight.dp)
            .onSizeChanged { size ->
                if (animationState.dragEnabled || animationState.contentVisibilityTarget == 1f) {
                    boxHeightAfterDrag = size.height.toFloat()
                } else {
                    boxHeightBeforeDrag = size.height.toFloat()
                }
            }
            .offset {
                val adjustment = getOffsetAdjustment()
                IntOffset(0, (boxOffset + adjustment).roundToInt())
            }
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(
                    topStart = Dimensions.CornerRadiusLarge,
                    topEnd = Dimensions.CornerRadiusLarge,
                    bottomStart = cornerRadius,
                    bottomEnd = cornerRadius
                ),
                ambientColor = Color.Black.copy(alpha = contentVisibility),
                spotColor = Color.Black.copy(alpha = contentVisibility),
                clip = false
            )
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
            // Draggable handle
            DragHandle(alpha = contentVisibility)

            Spacer(modifier = Modifier.height(Dimensions.MediumSpacing))

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

            // Difficulty badge
            DifficultyBadge(
                difficulty = plant.difficulty,
                alpha = contentVisibility
            )

            Spacer(modifier = Modifier.height(Dimensions.LargeSpacing))

            // Care Instructions Section Title
            Text(
                text = "Care Instructions",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = ScientificNameGray,
                modifier = Modifier.alpha(contentVisibility)
            )

            Spacer(modifier = Modifier.height(Dimensions.SmallSpacing))

            // Parse and display care instructions
            val careInstructions = remember(plant.careInstructions) {
                plant.careInstructions.split("\n").mapNotNull { line ->
                    val parts = line.split(":", limit = 2)
                    if (parts.size == 2) {
                        val label = parts[0].trim()
                        val value = parts[1].trim()
                        val icon = getCareIcon(label)
                        if (icon != null) Triple(icon, label, value) else null
                    } else null
                }
            }

            careInstructions.forEach { (icon, label, value) ->
                CareInstructionItem(
                    icon = icon,
                    label = label,
                    value = value,
                    alpha = contentVisibility
                )
                Spacer(modifier = Modifier.height(Dimensions.SmallSpacing))
            }

            Spacer(modifier = Modifier.height(Dimensions.MediumSpacing))

            // Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .alpha(contentVisibility)
                    .background(LightGray)
            )

            Spacer(modifier = Modifier.height(Dimensions.MediumSpacing))

            // Details
            Text(
                text = plant.details,
                fontSize = 13.sp,
                color = com.jan.composeset.ui.theme.DarkGray,
                modifier = Modifier.alpha(contentVisibility)
            )
        }
    }
}
