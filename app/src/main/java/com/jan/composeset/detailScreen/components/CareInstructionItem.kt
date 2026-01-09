package com.jan.composeset.detailScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jan.composeset.Dimensions
import com.jan.composeset.ui.theme.DarkGray
import com.jan.composeset.ui.theme.LightGray

@Composable
fun CareInstructionItem(
    icon: ImageVector,
    label: String,
    value: String,
    alpha: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.alpha(alpha),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(18.dp),
            tint = LightGray
        )

        Spacer(modifier = Modifier.width(Dimensions.SmallSpacing))

        Column {
            Text(
                text = "$label:",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = LightGray
            )
            Text(
                text = value,
                fontSize = 13.sp,
                color = DarkGray
            )
        }
    }
}
