package com.example.gameapp.ui.candleChart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun DrawScope.drawTransformedGrid(
    candleData: List<CandleStickData>,
    chartArea: Rect,
    chartState: CandlestickChartState,
    visibleRange: VisibleRange,
    config: ChartConfig
) {
    // Calculate appropriate grid spacing based on zoom level
    val priceGridLines = calculateOptimalGridLines(visibleRange.priceRange, chartState.scaleY)
    val priceStep = visibleRange.priceRange / priceGridLines

    // Draw horizontal grid lines (price levels)
    for (i in 0..priceGridLines) {
        val price = visibleRange.minPrice + (i * priceStep)
        val y = chartArea.bottom - ((price - visibleRange.minPrice) / visibleRange.priceRange * chartArea.height)

        if (y >= chartArea.top && y <= chartArea.bottom) {
            drawLine(
                color = config.gridColor,
                start = Offset(chartArea.left, y),
                end = Offset(chartArea.right, y),
                strokeWidth = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))
            )
        }
    }

    // Draw vertical grid lines (time intervals) - these move with horizontal pan/zoom
    val scaledCandleWidth = config.candleWidth * chartState.scaleX
    val scaledSpacing = config.candleSpacing * chartState.scaleX
    val candleFullWidth = scaledCandleWidth + scaledSpacing

    // Calculate time grid spacing based on zoom level
    val timeGridSpacing = calculateTimeGridSpacing(chartState.scaleX)

    for (i in visibleRange.startIndex..visibleRange.endIndex step timeGridSpacing) {
        val x = chartArea.left + chartState.offsetX + (i * candleFullWidth)

        if (x >= chartArea.left && x <= chartArea.right) {
            drawLine(
                color = config.gridColor,
                start = Offset(x, chartArea.top),
                end = Offset(x, chartArea.bottom),
                strokeWidth = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))
            )
        }
    }
}

fun DrawScope.drawTransformedPriceLabels(
    chartArea: Rect,
    chartState: CandlestickChartState,
    visibleRange: VisibleRange,
    config: ChartConfig
) {
    val priceGridLines = calculateOptimalGridLines(visibleRange.priceRange, chartState.scaleY)
    val priceStep = visibleRange.priceRange / priceGridLines

    for (i in 0..priceGridLines) {
        val price = visibleRange.minPrice + (i * priceStep)
        val y = chartArea.bottom - ((price - visibleRange.minPrice) / visibleRange.priceRange * chartArea.height)

        if (y >= chartArea.top && y <= chartArea.bottom) {
            drawContext.canvas.nativeCanvas.drawText(
                "$${String.format("%.2f", price)}",
                chartArea.left - 40f,
                y + 5f,
                android.graphics.Paint().apply {
                    color = config.textColor.copy(alpha = 0.7f).toArgb()
                    textSize = 12.sp.toPx()
                    isAntiAlias = true
                }
            )
        }
    }
}

fun DrawScope.drawTimeLabels(
    candleData: List<CandleStickData>,
    chartArea: Rect,
    chartState: CandlestickChartState,
    config: ChartConfig
) {
    val scaledCandleWidth = config.candleWidth * chartState.scaleX
    val scaledSpacing = config.candleSpacing * chartState.scaleX
    val candleFullWidth = scaledCandleWidth + scaledSpacing

    val timeGridSpacing = calculateTimeGridSpacing(chartState.scaleX)
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    val visibleStartIndex = maxOf(0, ((-chartState.offsetX) / candleFullWidth).toInt())
    val visibleEndIndex = minOf(
        candleData.size - 1,
        ((-chartState.offsetX + chartArea.width) / candleFullWidth).toInt()
    )

    for (i in visibleStartIndex..visibleEndIndex step timeGridSpacing) {
        val x = chartArea.left + chartState.offsetX + (i * candleFullWidth)

        if (x >= chartArea.left && x <= chartArea.right && i < candleData.size) {
            val timeString = dateFormat.format(Date(candleData[i].timestamp))

            drawContext.canvas.nativeCanvas.drawText(
                timeString,
                x - 20f,
                chartArea.bottom + 20f,
                android.graphics.Paint().apply {
                    color = config.textColor.copy(alpha = 0.7f).toArgb()
                    textSize = 10.sp.toPx()
                    isAntiAlias = true
                }
            )
        }
    }
}

fun DrawScope.drawCandlesticks(
    candleData: List<CandleStickData>,
    chartArea: Rect,
    chartState: CandlestickChartState,
    visibleRange: VisibleRange,
    config: ChartConfig
) {
    val scaledCandleWidth = config.candleWidth * chartState.scaleX
    val scaledSpacing = config.candleSpacing * chartState.scaleX
    val candleFullWidth = scaledCandleWidth + scaledSpacing

    candleData.forEachIndexed { index, candle ->
        val x = chartArea.left + chartState.offsetX + (index * candleFullWidth)

        // Skip if candle is outside visible area
        if (x < chartArea.left - scaledCandleWidth || x > chartArea.right + scaledCandleWidth) {
            return@forEachIndexed
        }

        // Calculate Y positions using visible range
        val openY = chartArea.bottom - ((candle.open - visibleRange.minPrice) / visibleRange.priceRange * chartArea.height)
        val closeY = chartArea.bottom - ((candle.close - visibleRange.minPrice) / visibleRange.priceRange * chartArea.height)
        val highY = chartArea.bottom - ((candle.high - visibleRange.minPrice) / visibleRange.priceRange * chartArea.height)
        val lowY = chartArea.bottom - ((candle.low - visibleRange.minPrice) / visibleRange.priceRange * chartArea.height)

        val color = if (candle.isBullish) config.bullishColor else config.bearishColor

        // Draw high-low line (wick)
        drawLine(
            color = color,
            start = Offset(x + scaledCandleWidth / 2, highY),
            end = Offset(x + scaledCandleWidth / 2, lowY),
            strokeWidth = 2.dp.toPx()
        )

        // Draw open-close rectangle (body)
        val bodyTop = minOf(openY, closeY)
        val bodyBottom = maxOf(openY, closeY)
        val bodyHeight = bodyBottom - bodyTop

        if (candle.isBullish) {
            // Bullish candle - hollow/outline
            drawRect(
                color = color,
                topLeft = Offset(x, bodyTop),
                size = Size(scaledCandleWidth, bodyHeight),
                style = Fill
            )
        } else {
            // Bearish candle - filled
            drawRect(
                color = color,
                topLeft = Offset(x, bodyTop),
                size = Size(scaledCandleWidth, bodyHeight),
                style = Fill
            )
        }

        // Highlight selected candle
        if (chartState.selectedCandle == candle) {
            drawRect(
                color = config.crosshairColor,
                topLeft = Offset(x - 2f, bodyTop - 2f),
                size = Size(scaledCandleWidth + 4f, bodyHeight + 4f),
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }
}

fun DrawScope.drawCrosshair(
    chartArea: Rect,
    crosshairX: Float,
    crosshairY: Float,
    config: ChartConfig
) {
    // Vertical line
    drawLine(
        color = config.crosshairColor,
        start = Offset(crosshairX, chartArea.top),
        end = Offset(crosshairX, chartArea.bottom),
        strokeWidth = 1.dp.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
    )

    // Horizontal line
    drawLine(
        color = config.crosshairColor,
        start = Offset(chartArea.left, crosshairY),
        end = Offset(chartArea.right, crosshairY),
        strokeWidth = 1.dp.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
    )
}

// Helper functions for optimal grid spacing
private fun calculateOptimalGridLines(priceRange: Float, scaleY: Float): Int {
    val scaledRange = priceRange * scaleY
    return when {
        scaledRange < 10 -> 5
        scaledRange < 50 -> 8
        scaledRange < 100 -> 10
        scaledRange < 500 -> 12
        else -> 15
    }.coerceIn(5, 20)
}

private fun calculateTimeGridSpacing(scaleX: Float): Int {
    return when {
        scaleX < 0.5f -> 20
        scaleX < 1f -> 10
        scaleX < 2f -> 5
        scaleX < 5f -> 2
        else -> 1
    }.coerceAtLeast(1)
}
