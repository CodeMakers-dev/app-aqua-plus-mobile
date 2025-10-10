package com.codemakers.aquaplus.ui.composables

import androidx.compose.runtime.Composable
import com.codemakers.aquaplus.ui.extensions.cop
import com.codemakers.aquaplus.ui.models.FeeSection

@Composable
fun DebtSection(
    fee: FeeSection,
    lastIndex: Int,
) {
    val weights = listOf(0.65f, 0.35f)
    TableHeader(
        headers = listOf("Descripción", "Valor"),
        weights = weights
    )
    fee.conceptos?.forEachIndexed { index, concept ->
        TableRow(
            cells = listOf(
                concept.title,
                concept.total.cop()
            ),
            weights = weights,
            bottomDivider = index != lastIndex
        )
    }
}