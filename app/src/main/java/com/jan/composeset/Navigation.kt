package com.jan.composeset

import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jan.composeset.detailScreen.PlantDetailScreen
import com.jan.composeset.listScreen.PlantListScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.PlantNavigation(
    navController: NavHostController,
    boundsTransform: BoundsTransform,
    cornerRadiusState: CornerRadiusState
) {
    val currentRadius by animateFloatAsState(
        targetValue = cornerRadiusState.cornerRadius,
        animationSpec = tween(durationMillis = AnimationConfig.BOUNDS_TRANSITION_MS),
        label = "cornerRadius"
    )

    val displayCutoutHeight = WindowInsets.displayCutout.asPaddingValues().calculateTopPadding()

    NavHost(
        navController = navController,
        startDestination = "list"
    ) {
        composable("list") {
            PlantListScreen(
                animatedVisibilityScope = this@composable,
                onPlantClick = {
                    navController.navigate("detail/${it}")
                },
                boundsTransform = boundsTransform,
                cornerRadius = currentRadius.dp,
                displayCutoutHeight = displayCutoutHeight,
            ).also {
                cornerRadiusState.updateRadius(Dimensions.CornerRadiusLarge.value)
            }
        }

        composable(
            route = "detail/{plantId}",
            arguments = listOf(
                navArgument("plantId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val plantId = backStackEntry.arguments?.getInt("plantId") ?: 1
            PlantDetailScreen(
                plantId = plantId,
                animatedVisibilityScope = this@composable,
                boundsTransform = boundsTransform,
                cornerRadius = currentRadius.dp,
                onBackClick = { navController.popBackStack() }
            ).also {
                cornerRadiusState.updateRadius(Dimensions.CornerRadiusNone.value)
            }
        }
    }
}
