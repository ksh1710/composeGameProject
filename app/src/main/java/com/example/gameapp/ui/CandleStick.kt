package com.example.gameapp.ui

import android.R.attr.textSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gameapp.ui.candleChart.CandleStickData
import com.example.gameapp.ui.candleChart.CandlestickChart
import com.example.gameapp.ui.candleChart.CandlestickChartState
import com.example.gameapp.ui.candleChart.ChartConfig
import com.example.gameapp.ui.candleChart.ChartControls
import com.example.gameapp.ui.candleChart.ChartFooter
import com.example.gameapp.ui.candleChart.ChartHeader
import com.example.gameapp.ui.theme.Pink40
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.div
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random
import kotlin.times

@Composable
fun CandlestickChartScreen(
    candleData: List<CandleStickData>,
    modifier: Modifier = Modifier,
    config: ChartConfig = ChartConfig()
) {
    val chartState = remember { CandlestickChartState() }

    // Initialize price range when data changes
    LaunchedEffect(candleData) {
        if (candleData.isNotEmpty()) {
            val minPrice = candleData.minOf { minOf(it.low, it.high, it.open, it.close) }
            val maxPrice = candleData.maxOf { maxOf(it.low, it.high, it.open, it.close) }
            chartState.initializePriceRange(minPrice, maxPrice)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(config.backgroundColor)
    ) {
        // Chart Header with selected candle info
        ChartHeader(
            selectedCandle = chartState.selectedCandle,
            config = config,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Main Chart Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            CandlestickChart(
                candleData = candleData,
                chartState = chartState,
                config = config,
                modifier = Modifier.fillMaxSize()
            )

            // Enhanced Zoom and Reset Controls
            ChartControls(
                onZoomInX = {
                    chartState.scaleX = (chartState.scaleX * 1.2f).coerceAtMost(config.maxZoomX)
                },
                onZoomOutX = {
                    chartState.scaleX = (chartState.scaleX / 1.2f).coerceAtLeast(config.minZoomX)
                },
                onZoomInY = {
                    chartState.scaleY = (chartState.scaleY * 1.2f).coerceAtMost(config.maxZoomY)
                },
                onZoomOutY = {
                    chartState.scaleY = (chartState.scaleY / 1.2f).coerceAtLeast(config.minZoomY)
                },
                onReset = { chartState.reset() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
        }

        // Chart Footer with volume or additional info
        ChartFooter(
            selectedCandle = chartState.selectedCandle,
            config = config,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}
