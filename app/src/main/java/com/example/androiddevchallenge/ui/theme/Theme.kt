package com.example.androiddevchallenge.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val myColors = lightColors(
    primary = Color.Black,
    secondary = Color.Black,
    surface = Color.Black,
    onSurface = Color.White,
    primaryVariant = Color.Black
)


@Composable
fun MyTheme(content: @Composable () -> Unit) {
    MaterialTheme(colors = myColors, typography = MaterialTheme.typography) {
        content()
    }
}
