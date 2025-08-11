package com.example.gameapp.ui.presentation.truthAndDareGame.truthOrDareGameScreen.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
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

            BottleStyle.CUSTOM_LUXURY -> {

                val bodyWidth = bottleWidth
                val bodyHeight = bottleHeight * 0.8f

                val centerX = size.width / 2f
                val startY = (size.height - bottleHeight) / 2f

                // Define Colors for a nice gradient
                val bottleColorTop = Color(0xFF03A9F4) // SaddleBrown
                val bottleColorBottom = Color(0xFF00BCD4) // Darker Brown
                val capColor = Color(0xFF00E7D0) // Light Grey
                val labelColor = Color(0xFFFFD255) // Lemon Chiffon

                drawRoundRect(
                    color = bottleColorTop,
                    topLeft = Offset(centerX - neckWidth / 2, startY),
                    size = Size(neckWidth, neckHeight),
                    cornerRadius = CornerRadius(x = 10f, y = 10f)
                )

                // 2. Bottle Body (using a Path for a more custom shape)
                val bodyTopY = startY + neckHeight
                val bodyBottomY = startY + bottleHeight

                Path().apply {
                    // Start at top-left of body
                    moveTo(centerX - bodyWidth / 2, bodyTopY)
                    // Top-right of body
                    lineTo(centerX + bodyWidth / 2, bodyTopY)
                    // Bottom-right corner round
                    arcTo(
                        rect = Rect(
                            left = centerX + bodyWidth / 2 - 40f,
                            top = bodyBottomY - 40f,
                            right = centerX + bodyWidth / 2,
                            bottom = bodyBottomY
                        ),
                        startAngleDegrees = 0f,
                        sweepAngleDegrees = 90f,
                        forceMoveTo = false
                    )
                    lineTo(centerX - bodyWidth / 2 + 40f, bodyBottomY)
                    // Bottom-left corner round
                    arcTo(
                        rect = Rect(
                            left = centerX - bodyWidth / 2,
                            top = bodyBottomY - 40f,
                            right = centerX - bodyWidth / 2 + 40f,
                            bottom = bodyBottomY
                        ),
                        startAngleDegrees = 90f,
                        sweepAngleDegrees = 90f,
                        forceMoveTo = false
                    )
                    close() // Close the path
                }

                // You can also use a simple drawRoundRect for the body if a complex path isn't needed:
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(bottleColorTop, bottleColorBottom),
                        startY = bodyTopY,
                        endY = bodyBottomY
                    ),
                    topLeft = Offset(centerX - bodyWidth / 2, bodyTopY),
                    size = Size(bodyWidth, bodyHeight),
                    cornerRadius = CornerRadius(x = 30f, y = 30f) // Rounded corners for the body
                )


                // 3. Bottle Cap
                drawRoundRect(
                    color = capColor,
                    topLeft = Offset(centerX - neckWidth / 2, startY / 2 + 80f),
                    size = Size(neckWidth, neckHeight / 2),
                    cornerRadius = CornerRadius(x = 10f, y = 10f)

//            radius = neckWidth / 2 + 5f,
//            center = Offset(centerX, startY)
                )

                // 4. Bottle Label (Rectangle in the middle of the body)
                val labelWidth = bottleWidth * 0.7f
                val labelHeight = bodyHeight * 0.3f
                val labelTopLeftX = centerX - labelWidth / 2
                val labelTopLeftY = bodyTopY + (bodyHeight - labelHeight) / 2

                drawRoundRect(
                    color = labelColor,
                    topLeft = Offset(labelTopLeftX, labelTopLeftY),
                    size = Size(labelWidth, labelHeight),
                    cornerRadius = CornerRadius(x = 10f, y = 10f)
                )

                // 5. Highlight/Reflection (Optional, for more realism)
//        drawOval(
//            color = Color.White.copy(alpha = 0.3f), // Semi-transparent white
//            topLeft = Offset(centerX - bottleWidth * 0.3f, startY + bottleHeight * 0.2f),
//            size = Size(bottleWidth * 0.2f, bottleHeight * 0.5f)
//        )

                // Add a simple border to the bottle for definition
//        drawRoundRect(
//            color = Color.Black.copy(alpha = 0.5f),
//            topLeft = Offset(centerX - bodyWidth / 2, bodyTopY),
//            size = Size(bodyWidth, bodyHeight),
//            cornerRadius = CornerRadius(x = 30f, y = 30f),
//            style = Stroke(width = 2f)
//        )
//        drawRoundRect(
//            color = Color.Black.copy(alpha = 0.5f),
//            topLeft = Offset(centerX - neckWidth / 2, startY),
//            size = Size(neckWidth, neckHeight),
//            cornerRadius = CornerRadius(x = 10f, y = 10f),
//            style = Stroke(width = 2f)
//        )

            }
        }
    }
}
