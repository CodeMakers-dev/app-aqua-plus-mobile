package com.codemakers.aquaplus.ui.modules.home.features.invoice

import com.codemakers.aquaplus.ui.models.Invoice

data class InvoiceUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val employeeRouteId: Int = -1,
    val invoice: Invoice? = null,
)
