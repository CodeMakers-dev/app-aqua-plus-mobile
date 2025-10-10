package com.codemakers.aquaplus.ui.modules.home.features.invoice

import androidx.lifecycle.viewModelScope
import com.codemakers.aquaplus.base.BaseViewModel
import com.codemakers.aquaplus.domain.usecases.GetInvoiceDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
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
    val state = _state.onStart {
        init()
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), initialState
    )

    //Init

    fun init() {
        getInvoice()
    }

    //Actions

    fun getInvoice() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
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
                        error = "error",
                    )
                }
            }
        }
    }
}