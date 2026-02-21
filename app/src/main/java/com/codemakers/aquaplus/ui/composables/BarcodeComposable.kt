package com.codemakers.aquaplus.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.style.TextAlign
import io.github.alexzhirkevich.qrose.oned.BarcodeType
import io.github.alexzhirkevich.qrose.oned.rememberBarcodePainter

@Composable
fun BarcodeComposable(value: String?, modifier: Modifier = Modifier) {
    if (!value.isNullOrEmpty()) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberBarcodePainter(
                    data = value,
                    type = BarcodeType.Code128,
                    onError = { ColorPainter(Color.Transparent) }
                ),
                contentDescription = value,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}