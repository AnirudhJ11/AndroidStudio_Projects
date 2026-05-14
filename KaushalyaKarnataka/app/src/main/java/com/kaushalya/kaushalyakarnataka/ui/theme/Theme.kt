package com.kaushalya.kaushalyakarnataka.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val Colors = lightColorScheme(
    primary = PrimaryBlue,
    secondary = Emerald
)

@Composable
fun KaushalyaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = Colors,
        typography = Typography,
        content = content
    )
}