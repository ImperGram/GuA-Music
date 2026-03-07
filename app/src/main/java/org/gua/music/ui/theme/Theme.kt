/*
 This is the source code for GuAMusic for Android.
 License: GNU General Public License v.3.

 Copyright 2026 Gleb Obitotsky
*/

package org.gua.music.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CustomFixedColorScheme = darkColorScheme(
    primary = AppPrimary,
    onPrimary = Color.Black,
    secondary = PurpleGrey80,
    tertiary = Pink80,

    background = AppBackground,
    surface = AppSurface,
    onBackground = OnAppBackground,
    onSurface = OnAppSurface,
)

@Composable
fun GuAMusicTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = CustomFixedColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}