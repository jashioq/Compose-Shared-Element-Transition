package com.jan.composeset

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp

@Composable
fun SharedTransitionScope.PlantListScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    onPlantClick: (Int) -> Unit,
    boundsTransform: BoundsTransform,
    cornerRadius: Dp,
    displayCutoutHeight: Dp,
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(plants) { plant ->
            PlantCard(
                plant = plant,
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = boundsTransform,
                onClick = {
                    onPlantClick(plant.id)
                },
                cornerRadius = cornerRadius,
                displayCutoutHeight = displayCutoutHeight,
            )
        }
    }
}

@Composable
fun SharedTransitionScope.PlantCard(
    plant: Plant,
    animatedVisibilityScope: AnimatedVisibilityScope,
    boundsTransform: BoundsTransform,
    onClick: () -> Unit,
    cornerRadius: Dp,
    displayCutoutHeight: Dp,
) {
    if (plant.id == 0) {
        Spacer(modifier = Modifier.height(displayCutoutHeight))
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .sharedElement(
                sharedContentState = rememberSharedContentState(key = "box-${plant.id}"),
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = boundsTransform,
            )
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp, bottomStart = cornerRadius, bottomEnd = cornerRadius))
            .background(Color(0xFFdfe6d5))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .aspectRatio(1f)
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = "image-${plant.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = boundsTransform,
                    )
                    .clip(RoundedCornerShape((cornerRadius - 8.dp).coerceAtLeast(0.dp)))
            ) {
                Image(
                    painter = painterResource(id = plant.imageRes),
                    contentDescription = plant.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = plant.name,
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(key = "name-${plant.id}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = boundsTransform,
                        ),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = plant.scientificName,
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.Gray,
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(key = "scientific-name-${plant.id}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = boundsTransform,
                        )
                )
            }
        }
    }
}
