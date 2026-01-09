package com.jan.composeset.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

// Plant app text styles
object PlantTextStyles {
    val PlantTitle = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )

    val PlantScientificName = TextStyle(
        fontSize = 18.sp,
        fontStyle = FontStyle.Italic,
        color = ScientificNameGray
    )

    val BadgeText = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
    )

    val CareLabel = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = LightGray
    )

    val CareValue = TextStyle(
        fontSize = 13.sp,
        color = DarkGray
    )

    val SectionTitle = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = ScientificNameGray
    )

    val PlantDetails = TextStyle(
        fontSize = 13.sp,
        color = DarkGray
    )
}