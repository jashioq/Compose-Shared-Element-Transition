package com.jan.composeset

import androidx.compose.ui.unit.dp

object AnimationConfig {
    const val BOUNDS_TRANSITION_MS = 1000
    const val CONTENT_FADE_MS = 500
    const val DEFAULT_DRAG_SPEED = 30F
    const val DRAG_OFFSET_MIN = -1000F
    const val DRAG_OFFSET_MAX = 0F
}

object Dimensions {
    val CardPadding = 16.dp
    val CardSpacing = 16.dp
    val ImageContainerWidth = 100.dp
    val CornerRadiusLarge = 32.dp
    val CornerRadiusNone = 0.dp
    val ContentPadding = 16.dp
    val SmallSpacing = 4.dp
    val MediumSpacing = 8.dp
    val LargeSpacing = 16.dp
    val BackButtonSize = 50.dp

    // Detail screen specific dimensions
    val FadeGradientHeight = 32.dp
    val DragHandleWidth = 40.dp
    val DragHandleHeight = 4.dp
    val BadgeHorizontalPadding = 10.dp
    val BadgeVerticalPadding = 4.dp
    val CareIconSize = 18.dp
    val DividerHeight = 1.dp
    val ShadowElevation = 16.dp
}
