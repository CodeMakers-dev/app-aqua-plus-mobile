package com.codemakers.aquaplus.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
                modifier = Modifier.weight(weights[i]),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
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
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        cells.forEachIndexed { i, text ->
            Text(
                text = text,
                modifier = Modifier.weight(weights[i]),
                style = MaterialTheme.typography.bodySmall.copy(
                    textAlign = TextAlign.Center
                ),
                textAlign = if (i == 0) TextAlign.Start else TextAlign.Center
            )
        }
    }
    if (bottomDivider) {
        HorizontalDivider(thickness = 1.dp, color = Border)
    }
}