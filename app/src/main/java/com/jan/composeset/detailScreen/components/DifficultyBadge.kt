package com.jan.composeset.detailScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jan.composeset.Difficulty
import com.jan.composeset.ui.theme.DifficultyEasy
import com.jan.composeset.ui.theme.DifficultyHard
import com.jan.composeset.ui.theme.DifficultyMedium

@Composable
fun DifficultyBadge(
    difficulty: Difficulty,
    alpha: Float,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (difficulty) {
        Difficulty.EASY -> DifficultyEasy
        Difficulty.MEDIUM -> DifficultyMedium
        Difficulty.HARD -> DifficultyHard
    }

    val textColor = when (difficulty) {
        Difficulty.EASY -> Color(0xFF2D6A4F)
        Difficulty.MEDIUM -> Color(0xFF8B7500)
        Difficulty.HARD -> Color(0xFFC1666B)
    }

    Box(
        modifier = modifier
            .alpha(alpha)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = difficulty.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
