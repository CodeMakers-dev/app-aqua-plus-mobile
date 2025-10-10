package com.codemakers.aquaplus.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TwoPane(
    left: @Composable () -> Unit,
    right: @Composable () -> Unit,
    gap: Dp = 16.dp
) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        if (maxWidth < 379.dp) {
            Column(verticalArrangement = Arrangement.spacedBy(gap)) {
                left(); right()
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(gap)
            ) {
                Box(Modifier.weight(0.5f)) { left() }
                Box(Modifier.weight(0.5f)) { right() }
            }
        }
    }
}