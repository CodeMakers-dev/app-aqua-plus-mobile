package com.codemakers.aquaplus.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codemakers.aquaplus.ui.theme.AquaPlusTheme

@Composable
fun LoadingWidget(
    isLoading: Boolean = true,
) {
    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Gray.copy(alpha = 0.7f))
                .clickable(enabled = false, onClick = {}),
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(60.dp, 60.dp)
            )
        }
    }
}

@Preview
@Composable
fun LoadingWidgetPreview() {
    AquaPlusTheme {
        LoadingWidget(
            isLoading = true
        )
    }
}