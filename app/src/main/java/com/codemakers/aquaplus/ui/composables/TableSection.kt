package com.codemakers.aquaplus.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codemakers.aquaplus.ui.theme.Border
import com.codemakers.aquaplus.ui.theme.LightRow
import com.codemakers.aquaplus.ui.theme.primaryColor

@Composable
fun TableHeader(
    headers: List<String>,
    weights: List<Float>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(LightRow)
            .border(1.dp, primaryColor, RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp)
    ) {
        headers.forEachIndexed { i, text ->
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.weight(weights[i]),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TableRow(
    cells: List<String>,
    weights: List<Float>,
    bottomDivider: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        cells.forEachIndexed { i, text ->
            Text(
                text = text,
                fontSize = 13.sp,
                modifier = Modifier.weight(weights[i]),
                textAlign = if (i == 0) TextAlign.Start else TextAlign.Center
            )
        }
    }
    if (bottomDivider) {
        HorizontalDivider(thickness = 1.dp, color = Border)
    }
}