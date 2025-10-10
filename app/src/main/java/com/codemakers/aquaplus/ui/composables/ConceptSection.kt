package com.codemakers.aquaplus.ui.composables

import androidx.compose.runtime.Composable
import com.codemakers.aquaplus.ui.extensions.cop
import com.codemakers.aquaplus.ui.models.FeeSection

@Composable
fun ConceptSection(
    fee: FeeSection,
    lastIndex: Int,
) {
    val weights = listOf(0.31f, 0.18f, 0.25f, 0.25f)
    TableHeader(
        headers = listOf("Descripción", "Consumo", "Tarifa", "Valor"),
        weights = weights
    )
    fee.conceptos?.forEachIndexed { index, concept ->
        TableRow(
            cells = listOf(
                concept.title,
                concept.consumptionTotal?.let { "$it m³" } ?: "-",
                concept.tarifa?.cop().orEmpty(),
                concept.total.cop()
            ),
            weights = weights,
            bottomDivider = index != lastIndex
        )
    }
}