package com.zeni.core.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

fun Modifier.horizontalAnimationAppearance(): Modifier = composed {
    var isVisible by remember { mutableStateOf(false) }
    var isRemoved by remember { mutableStateOf(false) }

    var itemWidth by remember { mutableIntStateOf(1000) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    DisposableEffect(Unit) {
        onDispose {
            isRemoved = true
        }
    }

    val offsetX by animateFloatAsState(
        targetValue = when {
            isRemoved -> itemWidth.toFloat()
            isVisible -> 0f
            else -> -itemWidth.toFloat()
        },
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        )
    )

    this
        .onSizeChanged { size -> itemWidth = size.width * 2 }
        .offset { IntOffset(offsetX.roundToInt(), 0) }
}