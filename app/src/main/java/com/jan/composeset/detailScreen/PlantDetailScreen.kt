package com.jan.composeset.detailScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jan.composeset.plants

@Composable
fun SharedTransitionScope.PlantDetailScreen(
    plantId: Int,
    animatedVisibilityScope: AnimatedVisibilityScope,
    cornerRadius: Dp,
    onBackClick: () -> Unit,
) {
    val plant = plants.firstOrNull { it.id == plantId } ?: return

    val viewModel: PlantDetailViewModel = viewModel()

    BackHandler(onBack = {
        viewModel.animateExit(onBackClick)
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        PlantDetailContent(
            plant = plant,
            animatedVisibilityScope = animatedVisibilityScope,
            viewModel = viewModel,
            cornerRadius = cornerRadius,
            onBackClick = onBackClick
        )
    }
}
