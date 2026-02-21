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

private fun monthName(mes: String?): String {
    val month = mes?.substringAfterLast("-")?.trimStart('0') ?: return ""
    return when (month) {
        "1" -> "Ene"
        "2" -> "Feb"
        "3" -> "Mar"
        "4" -> "Abr"
        "5" -> "May"
        "6" -> "Jun"
        "7" -> "Jul"
        "8" -> "Ago"
        "9" -> "Sep"
        "10" -> "Oct"
        "11" -> "Nov"
        "12" -> "Dic"
        else -> mes.substringBefore(" ")
    }
}

@Composable
fun BarsHistory(
    data: List<HistoryEntry>,
    maxBarHeight: Dp = 120.dp,
    barWidth: Dp = 30.dp,
    showPriceInK: Boolean = false,
    showPrice: Boolean = true
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
        // Barras con consumo flotando justo encima de cada una
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                data.forEachIndexed { index, item ->
                    val isLast = index == lastIndex
                    val barColor = if (isLast) lastBarColor else secondaryColor
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${item.consumo}",
                            fontSize = 8.sp,
                            lineHeight = 8.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(barWidth)
                        )
                        Canvas(
                            modifier = Modifier
                                .height(((item.consumo ?: 0).toFloat() / maxValue) * maxBarHeight)
                                .width(barWidth)
                        ) {
                            drawRect(color = barColor)
                        }
                    }
                }
            }

            // Línea horizontal de promedio anclada a la zona de barras
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(maxBarHeight)
                    .align(Alignment.BottomCenter)
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
                        text = monthName(item.mes),
                        fontSize = 9.sp,
                        lineHeight = 10.sp,
                        textAlign = TextAlign.Center
                    )
                    if (showPrice) {
                        if (showPriceInK) {
                            val k = item.precio?.div(1000)
                            if (k != null) {
                                Text(
                                    text = "${"%,.0f".format(k)}k",
                                    fontSize = 8.sp,
                                    lineHeight = 10.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            Text(
                                text = item.precio?.cop().orEmpty(),
                                fontSize = 8.sp,
                                lineHeight = 10.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
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
            HistoryEntry(consumo = 101, mes = "2026-01", precio = 120000.0),
            HistoryEntry(consumo = 50, mes = "2026-02", precio = 65000.0),
            HistoryEntry(consumo = 46, mes = "2026-03", precio = 58000.0),
            HistoryEntry(consumo = 77, mes = "2026-04", precio = 95000.0),
            HistoryEntry(consumo = 40, mes = "2026-05", precio = 50000.0),
            HistoryEntry(consumo = 33, mes = "2026-06", precio = 42000.0),
            HistoryEntry(consumo = 71, mes = "2026-07", precio = 88000.0),
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

@Preview(showBackground = true)
@Composable
fun BarsHistoryPreview2() {
    AquaPlusTheme {
        val sampleData = listOf(
            HistoryEntry(consumo = 25, mes = "2026-01", precio = 100000.0),
            HistoryEntry(consumo = 13, mes = "2026-02", precio = 52000.0),
            HistoryEntry(consumo = 17, mes = "2026-03", precio = 68000.0),
            HistoryEntry(consumo = 15, mes = "2026-04", precio = 60000.0),
            HistoryEntry(consumo = 20, mes = "2026-05", precio = 80000.0),
            HistoryEntry(consumo = 287, mes = "2026-06", precio = 1148000.0),
            HistoryEntry(consumo = 77, mes = "2026-07", precio = 149761.50),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)

        ) {
            BarsHistory(
                data = sampleData,
                showPriceInK = true,
                showPrice = false
            )
        }
    }
}