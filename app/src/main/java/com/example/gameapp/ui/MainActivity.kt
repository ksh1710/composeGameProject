package com.example.gameapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.gameapp.ui.candleChart.CandleStickData
import com.example.gameapp.ui.navigation.NavigationGraph
import com.example.gameapp.ui.theme.GameAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            GameAppTheme {
                NavigationGraph(navController = navController)
            }
        }
    }
}

@Composable
fun CandlestickChartExample() {
    // Sample data generation
    val sampleData = remember {
        generateSampleCandleData()
    }

    CandlestickChartScreen(
        candleData = sampleData,
        modifier = Modifier.fillMaxSize()
    )
}

// Helper function to generate sample data
private fun generateSampleCandleData(): List<CandleStickData> {
    val data = mutableListOf<CandleStickData>()
    var price = 100f
    val random = Random(System.currentTimeMillis())

    repeat(100) { i ->
        val change = (random.nextFloat() - 0.5f) * 10f
        val open = price
        val close = price + change
        val high = maxOf(open, close) + random.nextFloat() * 5f
        val low = minOf(open, close) - random.nextFloat() * 5f

        data.add(
            CandleStickData(
                timestamp = System.currentTimeMillis() + (i * 3600000), // 1 hour intervals
                open = open,
                high = high,
                low = low,
                close = close,
                volume = (random.nextLong() % 1000000).coerceAtLeast(10000)
            )
        )

        price = close
    }

    return data
}