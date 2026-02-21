package com.codemakers.aquaplus.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.codemakers.aquaplus.ui.extensions.cop
import com.codemakers.aquaplus.ui.models.HistoryEntry
import com.codemakers.aquaplus.ui.theme.AquaPlusTheme
import com.codemakers.aquaplus.ui.theme.secondaryColor
import kotlin.math.max

private val lastBarColor = Color(0xFFFF8C00)
private val averageLineColor = Color(0xFFFF8C00)

@Composable
fun BarsHistory(
    data: List<HistoryEntry>,
    maxBarHeight: Dp = 120.dp,
    barWidth: Dp = 30.dp,
    barSpacing: Dp = 24.dp
) {
    val maxValue = max(1, data.maxOfOrNull { it.consumo ?: 0 } ?: 1)
    val average = if (data.isEmpty()) 0f else data.mapNotNull { it.consumo }.average().toFloat()
    val lastIndex = data.lastIndex

    val avgRatio = average / maxValue

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 8.dp, start = 4.dp, end = 4.dp)
    ) {
        // Zona de barras con overlay de línea de promedio
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxBarHeight)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                data.forEachIndexed { index, item ->
                    val isLast = index == lastIndex
                    val barColor = if (isLast) lastBarColor else secondaryColor
                    Canvas(
                        modifier = Modifier
                            .height(((item.consumo ?: 0).toFloat() / maxValue) * maxBarHeight)
                            .width(barWidth)
                    ) {
                        drawLine(
                            color = barColor,
                            start = Offset(size.width / 2, size.height),
                            end = Offset(size.width / 2, 0f),
                            strokeWidth = size.width,
                            cap = StrokeCap.Square
                        )
                    }
                    Spacer(Modifier.width(barSpacing))
                }
            }

            // Línea horizontal de promedio
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(maxBarHeight)
                    .padding(bottom = 34.dp)
            ) {
                val avgY = size.height * (1f - avgRatio)
                drawLine(
                    color = averageLineColor,
                    start = Offset(0f, avgY),
                    end = Offset(size.width, avgY),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
                )
            }
        }

        // Etiquetas debajo de las barras
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            data.forEach { item ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(barWidth)
                ) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "${item.consumo} m³",
                        fontSize = 10.sp,
                        lineHeight = 10.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = item.mes?.substringBefore(" ").orEmpty(),
                        fontSize = 9.sp,
                        lineHeight = 10.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = item.precio?.cop().orEmpty(),
                        fontSize = 8.sp,
                        lineHeight = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.width(barSpacing))
            }
        }

        Spacer(Modifier.height(4.dp))
        Text(
            text = "Promedio: ${"%,.1f".format(average)} m³",
            fontSize = 11.sp,
            color = averageLineColor,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BarsHistoryPreview() {
    AquaPlusTheme {
        val sampleData = listOf(
            HistoryEntry(consumo = 101, mes = "Ago 2024", precio = 120000.0),
            HistoryEntry(consumo = 50, mes = "Sep 2024", precio = 65000.0),
            HistoryEntry(consumo = 46, mes = "Oct 2024", precio = 58000.0),
            HistoryEntry(consumo = 77, mes = "Nov 2024", precio = 95000.0),
            HistoryEntry(consumo = 40, mes = "Dic 2024", precio = 50000.0),
            HistoryEntry(consumo = 33, mes = "Ene 2025", precio = 42000.0),
            HistoryEntry(consumo = 71, mes = "Feb 2025", precio = 88000.0),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)

        ) {
            BarsHistory(data = sampleData)
        }
    }
}