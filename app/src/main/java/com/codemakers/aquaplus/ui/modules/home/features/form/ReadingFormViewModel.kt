package com.codemakers.aquaplus.ui.modules.home.features.form

import androidx.lifecycle.viewModelScope
import com.codemakers.aquaplus.base.BaseViewModel
import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.usecases.CreateOrUpdateReadingFormData
import com.codemakers.aquaplus.domain.usecases.GetEmployeeRouteByIdUseCase
import com.codemakers.aquaplus.domain.usecases.GetReadingFormDataByEmployeeRouteId
import com.codemakers.aquaplus.domain.usecases.SaveInvoiceUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class ReadingFormViewModel(
    employeeRouteId: Int,
    private val getEmployeeRouteByIdUseCase: GetEmployeeRouteByIdUseCase,
    private val getReadingFormDataByEmployeeRouteId: GetReadingFormDataByEmployeeRouteId,
    private val createOrUpdateReadingFormData: CreateOrUpdateReadingFormData,
    private val saveInvoiceUseCase: SaveInvoiceUseCase,
) : BaseViewModel() {

    private val initialState = ReadingFormUiState(
        employeeRouteId = employeeRouteId,
    )

    private val _state = MutableStateFlow(initialState)
    val state = _state.stateIn(
        viewModelScope, SharingStarted.Lazily, initialState
    )

    init {
        loadDataFromId(state.value.employeeRouteId)
    }

    // Helper methods
    private fun isProjectedReadingState(meterStateId: Int?): Boolean {
        return meterStateId == 25 || meterStateId == 26
    }

    private fun getMeterReadingForState(
        meterStateId: Int?,
        projectedReading: Double?,
        fallbackReading: String = ""
    ): String {
        return if (isProjectedReadingState(meterStateId)) {
            projectedReading?.toString().orEmpty()
        } else {
            fallbackReading
        }
    }

    //Actions

    fun cleanError() {
        _state.update { it.copy(error = null) }
    }

    fun cleanSuccess() {
        _state.update { it.copy(isCreatedOrUpdatedSuccess = false, isLoading = false) }
    }

    fun onSerialChange(serial: String) {
        _state.update { it.copy(serial = serial) }
    }

    fun onMeterReadingChange(meterReading: String) {
        // Only allow changes if meter state is not 25 or 26
        if (!isProjectedReadingState(state.value.meterStateId)) {
            _state.update { it.copy(meterReading = meterReading) }
        }
    }

    fun onAbnormalConsumptionChange(abnormalConsumption: Boolean) {
        _state.update { it.copy(abnormalConsumption = abnormalConsumption) }
    }

    fun onMeterStateChange(meterStateId: Int?) {
        _state.update { currentState ->
            val newState = currentState.copy(meterStateId = meterStateId)
            val projectedReading = currentState.route?.contador?.lecturaProyectada

            // Set meter reading based on state
            val meterReading = getMeterReadingForState(
                meterStateId = meterStateId,
                projectedReading = projectedReading
            )

            newState.copy(meterReading = meterReading)
        }
    }

    fun onObservationsChange(observations: String) {
        _state.update { it.copy(observations = observations) }
    }

    fun onSave() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                createOrUpdateReadingFormData(
                    employeeRouteId = state.value.employeeRouteId,
                    meterReading = state.value.meterReading,
                    abnormalConsumption = state.value.abnormalConsumption,
                    observations = state.value.observations,
                    readingFormDataId = state.value.readingFormDataId,
                    date = LocalDate.now(),
                    serial = state.value.serial,
                ).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _state.update {
                                it.copy(
                                    readingFormData = result.data,
                                )
                            }

                            // Generate and save invoice after successful reading form save
                            try {
                                val invoice = saveInvoiceUseCase(state.value.employeeRouteId)
                                if (invoice != null) {
                                    _state.update {
                                        it.copy(
                                            isCreatedOrUpdatedSuccess = true,
                                        )
                                    }
                                } else {
                                    _state.update {
                                        it.copy(
                                            isLoading = false,
                                            error = "Error al generar la factura",
                                        )
                                    }
                                }
                            } catch (e: Exception) {
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        error = "Error al guardar factura: ${e.message}",
                                    )
                                }
                            }
                        }

                        is Result.Error -> _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.error.message,
                            )
                        }

                        is Result.Exception -> _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.error.message,
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al guardar: ${e.message}",
                    )
                }
            }
        }
    }

    //Validations

    private fun loadDataFromId(employeeRouteId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                getEmployeeRouteByIdUseCase(employeeRouteId = employeeRouteId).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            val route = result.data.first
                            val config = result.data.second
                            val initialMeterStateId = route?.contador?.idEstadoContador
                            val projectedReading = route?.contador?.lecturaProyectada

                            _state.update {
                                var newState = it.copy(
                                    route = route,
                                    serial = route?.contador?.serial ?: "",
                                    config = config,
                                    meterStateId = initialMeterStateId,
                                )

                                // Set meter reading based on state
                                val meterReading = getMeterReadingForState(
                                    meterStateId = initialMeterStateId,
                                    projectedReading = projectedReading
                                )
                                newState = newState.copy(meterReading = meterReading)

                                newState
                            }
                            loadReadingFormData(employeeRouteId = employeeRouteId)
                        }

                        is Result.Error -> _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.error.message,
                            )
                        }

                        is Result.Exception -> _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.error.message,
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar datos: ${e.message}",
                    )
                }
            }
        }
    }

    private fun loadReadingFormData(employeeRouteId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getReadingFormDataByEmployeeRouteId(employeeRouteId = employeeRouteId).collect { result ->
                    when (result) {
                        is Result.Success -> _state.update { currentState ->
                            val meterStateId = currentState.meterStateId
                            val projectedReading = currentState.route?.contador?.lecturaProyectada

                            // Use helper method to determine meter reading
                            val meterReading = getMeterReadingForState(
                                meterStateId = meterStateId,
                                projectedReading = projectedReading,
                                fallbackReading = result.data?.meterReading.orEmpty()
                            )

                            currentState.copy(
                                readingFormData = result.data,
                                meterReading = meterReading,
                                abnormalConsumption = result.data?.abnormalConsumption,
                                observations = result.data?.observations ?: "",
                                isLoading = false
                            )
                        }

                        is Result.Error -> _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.error.message,
                            )
                        }

                        is Result.Exception -> _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.error.message,
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar formulario: ${e.message}",
                    )
                }
            }
        }
    }
}