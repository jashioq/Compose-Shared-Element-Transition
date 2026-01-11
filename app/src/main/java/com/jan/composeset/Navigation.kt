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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jan.composeset.detailScreen.PlantDetailScreen
import com.jan.composeset.listScreen.PlantListScreen

/**
 * Navhost with shared element transitions and synchronized corner radius animation.
 *
 * Demonstrates animating a non-standard property (corner radius) alongside shared element bounds.
 *
 * Why This Works:
 * - Same duration (1000ms) keeps corner radius in sync with bounds
 * - Both list card AND detail screen receive currentRadius.dp parameter
 * - List card applies radius conditionally: if (plant.id == selectedPlantId) cornerRadius else CornerRadiusLarge
 * - Detail screen image and box both use the animated radius
 *
 * This technique can be applied to animate any property alongside shared element transitions.
 *
 * @param cornerRadiusState Shared state for corner radius animation (updated on navigation)
 * @param boundsTransform Shared element bounds animation (1000ms tween)
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.PlantNavigation(
    navController: NavHostController,
    boundsTransform: BoundsTransform,
    cornerRadiusState: CornerRadiusState
) {
    val displayCutoutHeight = WindowInsets.displayCutout.asPaddingValues().calculateTopPadding()

    val currentRadius by animateFloatAsState(
        targetValue = cornerRadiusState.cornerRadius,
        animationSpec = tween(durationMillis = AnimationConfig.BOUNDS_TRANSITION_MS),
    )

    NavHost(
        navController = navController,
        startDestination = "list"
    ) {
        composable("list") {
            PlantListScreen(
                animatedVisibilityScope = this@composable,
                onPlantClick = { plantId ->
                    cornerRadiusState.updateRadius(Dimensions.CornerRadiusNone.value, plantId)
                    navController.navigate("detail/${plantId}")
                },
                boundsTransform = boundsTransform,
                cornerRadius = currentRadius.dp,
                selectedPlantId = cornerRadiusState.selectedPlantId,
                displayCutoutHeight = displayCutoutHeight,
            )
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
                cornerRadius = currentRadius.dp,
                onBackClick = {
                    cornerRadiusState.updateRadius(Dimensions.CornerRadiusLarge.value, plantId)
                    navController.popBackStack()
                }
            )
        }
    }
}
