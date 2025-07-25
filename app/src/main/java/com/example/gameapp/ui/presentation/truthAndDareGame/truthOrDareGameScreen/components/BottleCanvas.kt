package com.example.gameapp.ui.presentation.truthAndDareGame.truthOrDareGameScreen.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.gameapp.data.model.BottleDesign
import com.example.gameapp.data.model.BottleStyle


@Composable
fun BottleCanvas(
    design: BottleDesign,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val bottleWidth = size.width * 0.35f
        val bottleHeight = size.height * 0.9f
        val neckWidth = size.width * 0.15f
        val neckHeight = size.height * 0.2f

        when (design.style) {
            BottleStyle.CLASSIC -> {
                // Classic bottle shape
                drawRoundRect(
                    color = design.primaryColor,
                    topLeft = Offset(size.width * 0.325f, size.height * 0.25f),
                    size = Size(bottleWidth, bottleHeight),
                    cornerRadius = CornerRadius(12.dp.toPx())
                )
                // Neck
                drawRoundRect(
                    color = design.secondaryColor,
                    topLeft = Offset(size.width * 0.425f, size.height * 0.05f),
                    size = Size(neckWidth, neckHeight),
                    cornerRadius = CornerRadius(6.dp.toPx())
                )
            }

            BottleStyle.GLASS -> {
                // Glass bottle with highlight
                drawRoundRect(
                    color = design.primaryColor,
                    topLeft = Offset(size.width * 0.325f, size.height * 0.25f),
                    size = Size(bottleWidth, bottleHeight),
                    cornerRadius = CornerRadius(8.dp.toPx())
                )
                // Glass highlight
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.3f),
                    topLeft = Offset(size.width * 0.35f, size.height * 0.3f),
                    size = Size(bottleWidth * 0.3f, bottleHeight * 0.6f),
                    cornerRadius = CornerRadius(4.dp.toPx())
                )
                // Neck
                drawRoundRect(
                    color = design.secondaryColor,
                    topLeft = Offset(size.width * 0.425f, size.height * 0.05f),
                    size = Size(neckWidth, neckHeight),
                    cornerRadius = CornerRadius(6.dp.toPx())
                )
            }

            BottleStyle.MODERN -> {
                // Modern angular bottle
                drawRoundRect(
                    color = design.primaryColor,
                    topLeft = Offset(size.width * 0.325f, size.height * 0.25f),
                    size = Size(bottleWidth, bottleHeight),
                    cornerRadius = CornerRadius(4.dp.toPx())
                )
                // Modern neck
                drawRoundRect(
                    color = design.secondaryColor,
                    topLeft = Offset(size.width * 0.425f, size.height * 0.05f),
                    size = Size(neckWidth, neckHeight),
                    cornerRadius = CornerRadius(2.dp.toPx())
                )
            }

            BottleStyle.WINE -> {
                // Wine bottle shape (more elongated)
                drawRoundRect(
                    color = design.primaryColor,
                    topLeft = Offset(size.width * 0.35f, size.height * 0.3f),
                    size = Size(bottleWidth * 0.8f, bottleHeight * 0.85f),
                    cornerRadius = CornerRadius(16.dp.toPx())
                )
                // Wine bottle neck (longer)
                drawRoundRect(
                    color = design.secondaryColor,
                    topLeft = Offset(size.width * 0.45f, size.height * 0.05f),
                    size = Size(neckWidth * 0.7f, neckHeight * 1.5f),
                    cornerRadius = CornerRadius(8.dp.toPx())
                )
            }

            BottleStyle.CLEAR -> {
                // Clear bottle with border
                drawRoundRect(
                    color = design.primaryColor,
                    topLeft = Offset(size.width * 0.325f, size.height * 0.25f),
                    size = Size(bottleWidth, bottleHeight),
                    cornerRadius = CornerRadius(12.dp.toPx())
                )
                // Clear bottle outline
                drawRoundRect(
                    color = design.secondaryColor,
                    topLeft = Offset(size.width * 0.325f, size.height * 0.25f),
                    size = Size(bottleWidth, bottleHeight),
                    cornerRadius = CornerRadius(12.dp.toPx()),
                    style = Stroke(width = 3.dp.toPx())
                )
                // Neck
                drawRoundRect(
                    color = design.primaryColor,
                    topLeft = Offset(size.width * 0.425f, size.height * 0.05f),
                    size = Size(neckWidth, neckHeight),
                    cornerRadius = CornerRadius(6.dp.toPx())
                )
            }

            BottleStyle.LUXURY -> {
                // Luxury bottle with gradient effect
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(design.primaryColor, design.secondaryColor)
                    ),
                    topLeft = Offset(size.width * 0.325f, size.height * 0.25f),
                    size = Size(bottleWidth, bottleHeight),
                    cornerRadius = CornerRadius(12.dp.toPx())
                )
                // Decorative band
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.5f),
                    topLeft = Offset(size.width * 0.325f, size.height * 0.4f),
                    size = Size(bottleWidth, size.height * 0.05f),
                    cornerRadius = CornerRadius(2.dp.toPx())
                )
                // Neck
                drawRoundRect(
                    color = design.secondaryColor,
                    topLeft = Offset(size.width * 0.425f, size.height * 0.05f),
                    size = Size(neckWidth, neckHeight),
                    cornerRadius = CornerRadius(6.dp.toPx())
                )
            }
        }
    }
}
