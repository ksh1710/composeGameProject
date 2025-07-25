package com.example.gameapp.data.model
import androidx.compose.ui.graphics.Color



data class BottleDesign(
    val name: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val style: BottleStyle
)

enum class BottleStyle {
    CLASSIC, GLASS, MODERN, WINE, CLEAR, LUXURY
}
