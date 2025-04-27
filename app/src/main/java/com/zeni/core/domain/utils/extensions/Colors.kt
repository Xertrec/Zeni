package com.zeni.core.domain.utils.extensions

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Warning40 = Color(red = 255, green = 226, blue = 74)
val Warning80 = Color(red = 255, green = 222, blue = 161)

val ColorScheme.warning: Color @Composable
    get() = if (isSystemInDarkTheme()) Warning40 else Warning80