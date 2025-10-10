package com.codemakers.aquaplus.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.codemakers.aquaplus.ui.extensions.cop
import com.codemakers.aquaplus.ui.models.HistoryEntry
import com.codemakers.aquaplus.ui.theme.secondaryColor
import kotlin.math.max

@Composable
fun BarsHistory(
    data: List<HistoryEntry>,
    maxBarHeight: Dp = 120.dp,
    barWidth: Dp = 30.dp,
    barSpacing: Dp = 24.dp

) {
    val maxValue = max(1, data.maxOfOrNull { it.consumo } ?: 1)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                // Barra
                Canvas(
                    modifier = Modifier
                        .height((item.consumo.toFloat() / maxValue) * maxBarHeight)
                        .width(barWidth)
                ) {
                    drawLine(
                        color = secondaryColor,
                        start = Offset(size.width / 2, size.height),
                        end = Offset(size.width / 2, 0f),
                        strokeWidth = size.width,
                        cap = StrokeCap.Round
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text = item.mes.substringBefore(" "),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${item.consumo} m³",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = item.precio.cop(),
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(Modifier.height(barSpacing))

        }
    }
}