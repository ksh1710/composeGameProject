package com.example.gameapp.ui.candleChart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.min

class CandlestickChartState {
    var scaleX by mutableStateOf(1f)
    var scaleY by mutableStateOf(1f)
    var offsetX by mutableStateOf(0f)
    var offsetY by mutableStateOf(0f)
    var showCrosshair by mutableStateOf(false)
    var crosshairX by mutableStateOf(0f)
    var crosshairY by mutableStateOf(0f)
    var selectedCandle by mutableStateOf<CandleStickData?>(null)

    // Store the original price range for proper zoom calculations
    var originalMinPrice by mutableStateOf(0f)
    var originalMaxPrice by mutableStateOf(0f)
    var originalPriceRange by mutableStateOf(0f)

    fun reset() {
        scaleX = 1f
        scaleY = 1f
        offsetX = 0f
        offsetY = 0f
        showCrosshair = false
        selectedCandle = null
    }

    fun initializePriceRange(minPrice: Float, maxPrice: Float) {
        originalMinPrice = minPrice
        originalMaxPrice = maxPrice
        originalPriceRange = maxPrice - minPrice
    }
}

// Enhanced Chart Configuration
data class ChartConfig(
    val candleWidth: Float = 12f,
    val candleSpacing: Float = 4f,
    val minZoomX: Float = 0.3f,
    val maxZoomX: Float = 10f,
    val minZoomY: Float = 0.3f,
    val maxZoomY: Float = 10f,
    val zoomSensitivity: Float = 1.5f,
    val panSensitivity: Float = 1f,
    val bullishColor: Color = Color(0xFF4CAF50),
    val bearishColor: Color = Color(0xFFE53935),
    val gridColor: Color = Color(0xFF424242),
    val textColor: Color = Color(0xFF9E9E9E),
    val backgroundColor: Color = Color(0xFF121212),
    val crosshairColor: Color = Color(0xFF757575)
)

data class CandleStickData(
    val timestamp: Long,
    val open: Float,
    val high: Float,
    val low: Float,
    val close: Float,
    val volume: Long = 0
) {
    val isBullish: Boolean
        get() = close > open
}





data class OhlcData(val open: Float, val high: Float, val low: Float, val close: Float, val timestamp: Long)

@Composable
fun CandlestickChart(
    ohlcData: List<OhlcData>,
    modifier: Modifier = Modifier,
    candleWidthDp: Dp = 8.dp,
    candleSpacingDp: Dp = 4.dp,
    chartPadding: Dp = 16.dp // Padding around the chart for axes/labels
) {
    val density = LocalDensity.current
    val candleWidthPx = with(density) { candleWidthDp.toPx() }
    val candleSpacingPx = with(density) { candleSpacingDp.toPx() }
    val totalCandleSpacePx = candleWidthPx + candleSpacingPx
    val chartPaddingPx = with(density) { chartPadding.toPx() }

    // Calculate visible data range (you'd typically add logic for zooming/panning here)
    val visibleData = ohlcData // For simplicity, assume all data is visible

    if (visibleData.isEmpty()) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text("No data to display")
        }
        return
    }

    // Find min/max price for scaling Y-axis
    val minPrice = visibleData.minOfOrNull { it.low } ?: 0f
    val maxPrice = visibleData.maxOfOrNull { it.high } ?: 1f
    val priceRange = maxPrice - minPrice

    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val chartLeft = chartPaddingPx
        val chartTop = chartPaddingPx
        val chartRight = canvasWidth - chartPaddingPx
        val chartBottom = canvasHeight - chartPaddingPx
        val chartHeight = chartBottom - chartTop
        val chartDrawableWidth = chartRight - chartLeft

        if (priceRange <= 0f) { // Avoid division by zero
            // Handle case where all prices are the same
            return@Canvas
        }

        val pixelsPerPriceUnit = chartHeight / priceRange

        // Calculate total width needed for candles
        val totalCandlesWidth = visibleData.size * totalCandleSpacePx

        // Adjust starting X based on total candles and chart width
        // This centers the candles if they don't fill the entire width, or allows scrolling
        val startXOffset = if (totalCandlesWidth < chartDrawableWidth) {
            (chartDrawableWidth - totalCandlesWidth) / 2 + chartLeft
        } else {
            chartLeft // Or implement horizontal scrolling
        }


        visibleData.forEachIndexed { index, ohlc ->
            // X-coordinate for the center of the current candle
            val xCenter = startXOffset + (index * totalCandleSpacePx) + (totalCandleSpacePx / 2f)

            // Y-coordinates for the wick (shadow)
            val highY = chartBottom - ((ohlc.high - minPrice) * pixelsPerPriceUnit)
            val lowY = chartBottom - ((ohlc.low - minPrice) * pixelsPerPriceUnit)

            // Y-coordinates for the body
            val openY = chartBottom - ((ohlc.open - minPrice) * pixelsPerPriceUnit)
            val closeY = chartBottom - ((ohlc.close - minPrice) * pixelsPerPriceUnit)

            // Determine body top and bottom (Open/Close can be higher/lower)
            val bodyTop = min(openY, closeY)
            val bodyBottom = max(openY, closeY)

            // Candlestick color
            val candleColor = if (ohlc.close >= ohlc.open) Color.Green else Color.Red

            // Draw the wick
            drawLine(
                color = candleColor,
                start = Offset(x = xCenter, y = highY),
                end = Offset(x = xCenter, y = lowY),
                strokeWidth = 1.dp.toPx() // Thin line for wick
            )

            // Draw the body
            drawRect(
                color = candleColor,
                topLeft = Offset(x = xCenter - (candleWidthPx / 2f), y = bodyTop),
                size = Size(width = candleWidthPx, height = (bodyBottom - bodyTop).coerceAtLeast(1f)) // Ensure minimum height for flat candles
            )
        }

        // Optional: Draw Y-axis labels and grid lines
        // This involves more calculations, similar to the above, for specific price points.
        // For example, to draw horizontal grid lines:
        // val numGridLines = 5
        // for (i in 0..numGridLines) {
        //     val priceAtLine = minPrice + (priceRange / numGridLines) * i
        //     val yGridLine = chartBottom - ((priceAtLine - minPrice) * pixelsPerPriceUnit)
        //     drawLine(
        //         color = Color.Gray.copy(alpha = 0.3f),
        //         start = Offset(chartLeft, yGridLine),
        //         end = Offset(chartRight, yGridLine),
        //         strokeWidth = 0.5.dp.toPx()
        //     )
        //     // Draw text label for priceAtLine
        //     drawContext.canvas.nativeCanvas.drawText(
        //         "%.2f".format(priceAtLine),
        //         chartLeft - 40.dp.toPx(), // Adjust X position for label
        //         yGridLine + 5.dp.toPx(), // Adjust Y position for text baseline
        //         Paint().apply {
        //             color = android.graphics.Color.GRAY
        //             textSize = 12.sp.toPx()
        //         }
        //     )
        // }
    }
}