package com.jan.composeset.listScreen

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import com.jan.composeset.Dimensions
import com.jan.composeset.Plant
import com.jan.composeset.plants
import com.jan.composeset.ui.theme.PlantCardBackground
import com.jan.composeset.ui.theme.ScientificNameGray

@Composable
fun SharedTransitionScope.PlantListScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    onPlantClick: (Int) -> Unit,
    boundsTransform: BoundsTransform,
    cornerRadius: Dp,
    selectedPlantId: Int?,
    displayCutoutHeight: Dp,
) {
    LazyColumn(
        contentPadding = PaddingValues(Dimensions.CardPadding),
        verticalArrangement = Arrangement.spacedBy(Dimensions.CardSpacing),
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(plants) { plant ->
            if (plant.id == 0) {
                Spacer(modifier = Modifier.height(displayCutoutHeight))
            }
            PlantCard(
                plant = plant,
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = boundsTransform,
                onClick = {
                    onPlantClick(plant.id)
                },
                cornerRadius = if (plant.id == selectedPlantId) cornerRadius else Dimensions.CornerRadiusLarge,
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
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .sharedElement(
                sharedContentState = rememberSharedContentState(key = "box-${plant.id}"),
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = boundsTransform,
            )
            .clip(RoundedCornerShape(topStart = Dimensions.CornerRadiusLarge, topEnd = Dimensions.CornerRadiusLarge, bottomStart = cornerRadius, bottomEnd = cornerRadius))
            .background(PlantCardBackground)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(Dimensions.MediumSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(Dimensions.ImageContainerWidth)
                    .aspectRatio(1f)
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = "image-${plant.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = boundsTransform,
                    )
                    .clip(RoundedCornerShape((cornerRadius - Dimensions.MediumSpacing).coerceAtLeast(0.dp)))
            ) {
                Image(
                    painter = painterResource(id = plant.imageRes),
                    contentDescription = plant.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(Dimensions.LargeSpacing))

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

                Spacer(modifier = Modifier.height(Dimensions.SmallSpacing))

                Text(
                    text = plant.scientificName,
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Italic,
                    color = ScientificNameGray,
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
