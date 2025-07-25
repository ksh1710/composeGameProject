package com.example.gameapp.ui.candleChart

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.abs

suspend fun PointerInputScope.detectAxisPinchZoom(
    onZoom: (xZoom: Float, yZoom: Float) -> Unit,
    onPan: (Offset) -> Unit
) {
    detectTransformGestures { _, pan, zoom, _ ->
        onPan(pan)

        // Skip small zooms to avoid noise
        if (zoom < 0.99f || zoom > 1.01f) {
            // Use pan direction to infer zoom direction — fallback method
            val isMostlyHorizontal = abs(pan.x) > abs(pan.y)
            val xZoom = if (isMostlyHorizontal) zoom else 1f
            val yZoom = if (!isMostlyHorizontal) zoom else 1f

            onZoom(xZoom, yZoom)
        }
    }
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CandlestickChart(
    candleData: List<CandleStickData>,
    chartState: CandlestickChartState,
    config: ChartConfig,
    modifier: Modifier = Modifier
) {
    val scale by remember { mutableStateOf(1f) }
    val offset by remember { mutableStateOf(Offset.Zero) }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1280f / 959f)

    ) {
        Canvas(
            modifier = modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
//            .pointerInput(Unit) {
//                detectAxisPinchZoom(
//                    onZoom = { xZoom, yZoom ->
//                        // Enable both horizontal and vertical zoom
//                        chartState.scaleX = (chartState.scaleX * xZoom)
//                            .coerceIn(config.minZoomX, config.maxZoomX)
//                        chartState.scaleY = (chartState.scaleY * yZoom)
//                            .coerceIn(config.minZoomY, config.maxZoomY)
//                    },
//                    onPan = { pan ->
//                        chartState.offsetX += pan.x
//                        chartState.offsetY += pan.y
//                    }
//                )
//            }
//            .pointerInput(Unit) {
//                detectTapGestures(
//                    onTap = { offset ->
//                        chartState.showCrosshair = true
//                        chartState.crosshairX = offset.x
//                        chartState.crosshairY = offset.y
//
//                        // Find selected candle based on transformed coordinates
//                        val chartArea = Rect(
//                            offset = Offset(50f, 50f),
//                            size = Size(size.width - 100f, size.height - 100f)
//                        )
//                        val scaledCandleWidth = config.candleWidth * chartState.scaleX
//                        val scaledSpacing = config.candleSpacing * chartState.scaleX
//                        val candleIndex = ((offset.x - chartArea.left - chartState.offsetX) /
//                                (scaledCandleWidth + scaledSpacing)).toInt()
//
//                        if (candleIndex in candleData.indices) {
//                            chartState.selectedCandle = candleData[candleIndex]
//                        }
//                    }
//                )
//            }
        ) {
            if (candleData.isEmpty()) return@Canvas

            val canvasWidth = size.width
            val canvasHeight = size.height
            val chartArea = Rect(
                offset = Offset(50f, 50f),
                size = Size(canvasWidth - 100f, canvasHeight - 100f)
            )

            // Calculate visible data range and price range
            val visibleRange = calculateVisibleRange(candleData, chartArea, chartState, config)

            // Draw grid with proper transformation
            drawTransformedGrid(
                candleData = candleData,
                chartArea = chartArea,
                chartState = chartState,
                visibleRange = visibleRange,
                config = config
            )

            // Draw price labels with proper transformation
            drawTransformedPriceLabels(
                chartArea = chartArea,
                chartState = chartState,
                visibleRange = visibleRange,
                config = config
            )

            // Draw time labels
            drawTimeLabels(
                candleData = candleData,
                chartArea = chartArea,
                chartState = chartState,
                config = config
            )

            // Draw candlesticks
            drawCandlesticks(
                candleData = candleData,
                chartArea = chartArea,
                chartState = chartState,
                visibleRange = visibleRange,
                config = config
            )

            // Draw crosshair
            if (chartState.showCrosshair) {
                drawCrosshair(
                    chartArea = chartArea,
                    crosshairX = chartState.crosshairX,
                    crosshairY = chartState.crosshairY,
                    config = config
                )
            }
        }
    }
}

data class VisibleRange(
    val minPrice: Float,
    val maxPrice: Float,
    val priceRange: Float,
    val startIndex: Int,
    val endIndex: Int
)

fun calculateVisibleRange(
    candleData: List<CandleStickData>,
    chartArea: Rect,
    chartState: CandlestickChartState,
    config: ChartConfig
): VisibleRange {
    val scaledCandleWidth = config.candleWidth * chartState.scaleX
    val scaledSpacing = config.candleSpacing * chartState.scaleX
    val candleFullWidth = scaledCandleWidth + scaledSpacing

    // Calculate visible candle indices
    val startIndex = maxOf(0, ((-chartState.offsetX) / candleFullWidth).toInt() - 1)
    val endIndex = minOf(
        candleData.size - 1,
        ((-chartState.offsetX + chartArea.width) / candleFullWidth).toInt() + 1
    )

    // Calculate visible price range
    val visibleCandles = if (startIndex <= endIndex) {
        candleData.subList(startIndex, endIndex + 1)
    } else {
        candleData
    }

    val minPrice = visibleCandles.minOfOrNull { minOf(it.low, it.high, it.open, it.close) }
        ?: chartState.originalMinPrice
    val maxPrice = visibleCandles.maxOfOrNull { maxOf(it.low, it.high, it.open, it.close) }
        ?: chartState.originalMaxPrice

    // Apply vertical zoom to price range
    val basePriceRange = maxPrice - minPrice
    val zoomedPriceRange = basePriceRange / chartState.scaleY
    val priceCenter = minPrice + basePriceRange / 2

    // Apply vertical offset
    val offsetPriceRange = zoomedPriceRange * (chartState.offsetY / chartArea.height)
    val adjustedMinPrice = priceCenter - zoomedPriceRange / 2 - offsetPriceRange
    val adjustedMaxPrice = priceCenter + zoomedPriceRange / 2 - offsetPriceRange

    return VisibleRange(
        minPrice = adjustedMinPrice,
        maxPrice = adjustedMaxPrice,
        priceRange = adjustedMaxPrice - adjustedMinPrice,
        startIndex = startIndex,
        endIndex = endIndex
    )
}
