package com.zeni.core.presentation.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize


/**
 * Modifier that applies a shimmer effect to the background of the composable.
 */
fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "ShimmerTransition")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Restart
        ),
        label = "ShimmerXAnimation"
    )

    background(
        brush = Brush.linearGradient(
            colors = if (isSystemInDarkTheme()) shimmerDarkColors else shimmerLightColors,
            start = Offset(x = startOffsetX, y = 0f),
            end = Offset(x = startOffsetX + size.width.toFloat(), y = size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}

/**
 * List of light colors for the shimmer effect.
 */
val shimmerLightColors = listOf(
    Color.LightGray,
    Color.Gray,
    Color.LightGray
)

/**
 * List of dark colors for the shimmer effect.
 */
val shimmerDarkColors = listOf(
    Color.DarkGray,
    Color.Gray,
    Color.DarkGray
)