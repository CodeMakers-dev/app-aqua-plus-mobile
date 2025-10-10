package com.codemakers.aquaplus.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun KeyValueRow(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2
        )
    }
}

@Composable
fun InfoCard(
    title: String, content:
    @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(Modifier.padding(8.dp)) {
            if (title.isNotBlank()) {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(8.dp))
            }
            content()
        }
    }
}