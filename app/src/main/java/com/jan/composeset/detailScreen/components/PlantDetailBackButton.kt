package com.jan.composeset.detailScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import com.jan.composeset.Dimensions

@Composable
fun PlantDetailBackButton(
    onClick: () -> Unit,
    alpha: Float,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .padding(Dimensions.ContentPadding)
            .alpha(alpha)
            .background(
                Color.Black.copy(alpha = 0.3f),
                RoundedCornerShape(Dimensions.BackButtonSize)
            )
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.Black,
            modifier = Modifier.fillMaxSize()
        )
    }
}
