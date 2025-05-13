package com.zeni.core.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun CheckmarkAnimation(
    modifier: Modifier = Modifier,
    circleColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
    checkmarkColor: Color = MaterialTheme.colorScheme.primary,
    animationDuration: Int = 1000
) {
    val checkAnimatable = remember { Animatable(0f) }
    val circleAnimatable = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        circleAnimatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = animationDuration,
                easing = FastOutSlowInEasing
            )
        )
        delay(150)

        checkAnimatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = animationDuration,
                easing = FastOutLinearInEasing
            )
        )
    }

    Box(
        modifier = modifier
            .padding(16.dp)
            .size(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(100.dp)) {
            val circleRadius = size.minDimension / 2 * circleAnimatable.value
            drawCircle(
                color = circleColor,
                radius = circleRadius,
                style = Stroke(width = 8f)
            )

            if (circleAnimatable.value >= 0.9f && checkAnimatable.value > 0f) {
                val startPoint = Offset(x = size.width * 0.3f, y = size.height * 0.5f)
                val middlePoint = Offset(x = size.width * 0.45f, y = size.height * 0.65f)
                val endPoint = Offset(x = size.width * 0.7f, y = size.height * 0.35f)
                val progress1 = (checkAnimatable.value * 2).coerceAtMost(1f)
                val progress2 = ((checkAnimatable.value * 2) - 1f).coerceIn(0f, 1f)

                if (progress1 > 0f) {
                    val x1 = startPoint.x + (middlePoint.x - startPoint.x) * progress1
                    val y1 = startPoint.y + (middlePoint.y - startPoint.y) * progress1
                    
                    drawLine(
                        color = checkmarkColor,
                        start = startPoint,
                        end = Offset(x1, y1),
                        strokeWidth = 12f,
                        cap = StrokeCap.Round
                    )
                }

                if (progress2 > 0f) {
                    val x2 = middlePoint.x + (endPoint.x - middlePoint.x) * progress2
                    val y2 = middlePoint.y + (endPoint.y - middlePoint.y) * progress2
                    
                    drawLine(
                        color = checkmarkColor,
                        start = middlePoint,
                        end = Offset(x2, y2),
                        strokeWidth = 12f,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}
