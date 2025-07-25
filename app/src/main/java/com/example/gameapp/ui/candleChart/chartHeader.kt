package com.example.gameapp.ui.candleChart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// 5. Chart Header Component
@Composable
fun ChartHeader(
    selectedCandle: CandleStickData?,
    config: ChartConfig,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "STOCK PRICE",
                style = MaterialTheme.typography.titleMedium,
                color = config.textColor,
                fontWeight = FontWeight.Bold
            )

            if (selectedCandle != null) {
                Text(
                    text = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                        .format(Date(selectedCandle.timestamp)),
                    style = MaterialTheme.typography.bodySmall,
                    color = config.textColor.copy(alpha = 0.7f)
                )
            }
        }

        selectedCandle?.let { candle ->
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "$${String.format("%.2f", candle.close)}",
                    style = MaterialTheme.typography.titleLarge,
                    color = if (candle.isBullish) config.bullishColor else config.bearishColor,
                    fontWeight = FontWeight.Bold
                )

                val change = candle.close - candle.open
                val changePercent = (change / candle.open) * 100

                Text(
                    text = "${if (change >= 0) "+" else ""}${String.format("%.2f", change)} (${String.format("%.2f", changePercent)}%)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (candle.isBullish) config.bullishColor else config.bearishColor
                )
            }
        }
    }
}
