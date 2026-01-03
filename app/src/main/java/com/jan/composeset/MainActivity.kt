package com.jan.composeset

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jan.composeset.ui.theme.ComposeSETTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        hideSystemBars()
        setContent {
            ComposeSETTheme {
                SharedElementTransitionApp()
            }
        }
    }

    private fun hideSystemBars() {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedElementTransitionApp() {
    val navController = rememberNavController()

    val boundsTransform = BoundsTransform { _, _ ->
        tween(durationMillis = 1000)
    }

    val cornerRadiusState = remember { CornerRadiusState(32F) }

    val currentRadius by animateFloatAsState(
        targetValue = cornerRadiusState.cornerRadius,
        animationSpec = tween(durationMillis = 1000),
    )

    val displayCutoutHeight = WindowInsets.displayCutout.asPaddingValues().calculateTopPadding()

    SharedTransitionLayout(
        modifier = Modifier.fillMaxSize()
    ) {
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
                    cornerRadiusState.updateRadius(32F)
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
                    cornerRadiusState.updateRadius(0F)
                }
            }
        }
    }
}
