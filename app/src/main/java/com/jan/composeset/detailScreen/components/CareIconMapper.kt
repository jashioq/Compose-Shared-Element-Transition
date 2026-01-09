package com.jan.composeset.detailScreen.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector

// Helper function to get care icon based on label
fun getCareIcon(label: String): ImageVector? {
    return when (label.lowercase()) {
        "water" -> Icons.Filled.WaterDrop
        "light" -> Icons.Filled.WbSunny
        "temp" -> Icons.Filled.Thermostat
        "tips" -> Icons.Filled.Lightbulb
        else -> null
    }
}
