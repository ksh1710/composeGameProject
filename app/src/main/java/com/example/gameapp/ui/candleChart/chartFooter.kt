package com.example.gameapp.ui.candleChart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ChartFooter(
    selectedCandle: CandleStickData?,
    config: ChartConfig,
    modifier: Modifier = Modifier
) {
    selectedCandle?.let { candle ->
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = Color.Black.copy(alpha = 0.8f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "OHLC Details",
                    style = MaterialTheme.typography.titleSmall,
                    color = config.textColor,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Open: $${String.format("%.2f", candle.open)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = config.textColor
                        )
                        Text(
                            text = "High: $${String.format("%.2f", candle.high)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = config.bullishColor
                        )
                    }

                    Column {
                        Text(
                            text = "Low: $${String.format("%.2f", candle.low)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = config.bearishColor
                        )
                        Text(
                            text = "Close: $${String.format("%.2f", candle.close)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (candle.isBullish) config.bullishColor else config.bearishColor
                        )
                    }

                    if (candle.volume > 0) {
                        Column {
                            Text(
                                text = "Volume",
                                style = MaterialTheme.typography.bodySmall,
                                color = config.textColor.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "${candle.volume}",
                                style = MaterialTheme.typography.bodySmall,
                                color = config.textColor
                            )
                        }
                    }
                }
            }
        }
    }
}
