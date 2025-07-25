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

@Composable
fun BottleDesign(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val bottleWidth = size.width * 0.2f // 40% of canvas width
        val bottleHeight = size.height * 0.7f // 70% of canvas height
        val neckWidth = bottleWidth * 0.3f
        val neckHeight = bottleHeight * 0.2f
        val bodyWidth = bottleWidth
        val bodyHeight = bottleHeight * 0.8f

        val centerX = size.width / 2f
        val startY = (size.height - bottleHeight) / 2f

        // Define Colors for a nice gradient
        val bottleColorTop = Color(0xFF03A9F4) // SaddleBrown
        val bottleColorBottom = Color(0xFF00BCD4) // Darker Brown
        val capColor = Color(0xFF00E7D0) // Light Grey
        val labelColor = Color(0xFFFFD255) // Lemon Chiffon

        // 1. Bottle Neck
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
