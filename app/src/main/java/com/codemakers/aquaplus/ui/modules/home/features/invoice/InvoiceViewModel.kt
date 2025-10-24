package com.codemakers.aquaplus.ui.modules.home.features.invoice

import androidx.lifecycle.viewModelScope
import com.codemakers.aquaplus.base.BaseViewModel
import com.codemakers.aquaplus.domain.usecases.GetInvoiceDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InvoiceViewModel(
    employeeRouteId: Int,
    private val getInvoiceDataUseCase: GetInvoiceDataUseCase
) : BaseViewModel() {

    private val initialState = InvoiceUiState(
        employeeRouteId = employeeRouteId,
    )

    private val _state = MutableStateFlow(initialState)
    val state = _state.stateIn(
        viewModelScope, SharingStarted.Lazily, initialState
    )

    private var isInitialized = false

    init {
        // Load data immediately in background to avoid UI lag
        loadInvoiceData()
    }

    //Actions

    private fun loadInvoiceData() {
        if (isInitialized) return
        isInitialized = true
        
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            
            try {
                val invoiceData = getInvoiceDataUseCase(state.value.employeeRouteId)

                if (invoiceData != null) {
                    _state.update {
                        it.copy(
                            invoice = invoiceData,
                            isLoading = false,
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "No se encontró información de factura",
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar la factura: ${e.message}",
                    )
                }
            }
        }
    }
    
    fun refreshInvoice() {
        isInitialized = false
        loadInvoiceData()
    }

    override fun onCleared() {
        super.onCleared()
        // Clear any cached data to prevent memory leaks
        _state.value = initialState
    }
}