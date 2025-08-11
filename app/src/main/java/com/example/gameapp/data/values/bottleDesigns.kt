package com.example.gameapp.data.values

import androidx.compose.ui.graphics.Color
import com.example.gameapp.data.model.BottleDesign
import com.example.gameapp.data.model.BottleStyle

val bottleDesigns = listOf(
    BottleDesign(
        name = "Classic Brown",
        primaryColor = Color(0xFF8B4513),
        secondaryColor = Color(0xFF654321),
        style = BottleStyle.CLASSIC
    ),
    BottleDesign(
        name = "Green Glass",
        primaryColor = Color(0xFF2E7D32),
        secondaryColor = Color(0xFF1B5E20),
        style = BottleStyle.GLASS
    ),
    BottleDesign(
        name = "Blue Bottle",
        primaryColor = Color(0xFF1976D2),
        secondaryColor = Color(0xFF0D47A1),
        style = BottleStyle.MODERN
    ),
    BottleDesign(
        name = "Red Wine",
        primaryColor = Color(0xFF8B0000),
        secondaryColor = Color(0xFF5D0000),
        style = BottleStyle.WINE
    ),
    BottleDesign(
        name = "Clear Glass",
        primaryColor = Color(0xFFE3F2FD),
        secondaryColor = Color(0xFFBBDEFB),
        style = BottleStyle.CLEAR
    ),
    BottleDesign(
        name = "Golden",
        primaryColor = Color(0xFFFFD700),
        secondaryColor = Color(0xFFB8860B),
        style = BottleStyle.LUXURY
    ),
    BottleDesign(
        name = "Custom Luxury",
        primaryColor = Color(0xFFFFD700),
        secondaryColor = Color(0xFFB8860B),
        style = BottleStyle.CUSTOM_LUXURY
    )

)
