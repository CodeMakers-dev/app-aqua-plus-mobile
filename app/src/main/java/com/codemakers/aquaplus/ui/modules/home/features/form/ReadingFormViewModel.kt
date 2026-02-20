package com.codemakers.aquaplus.ui.modules.home.features.form

import androidx.lifecycle.viewModelScope
import com.codemakers.aquaplus.base.BaseViewModel
import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.EmployeeRoute
import com.codemakers.aquaplus.domain.models.EmployeeRouteConfig
import com.codemakers.aquaplus.domain.models.ReadingFormData
import com.codemakers.aquaplus.domain.usecases.CreateOrUpdateReadingFormData
import com.codemakers.aquaplus.domain.usecases.GetEmployeeRouteByIdUseCase
import com.codemakers.aquaplus.domain.usecases.GetReadingFormDataByEmployeeRouteId
import com.codemakers.aquaplus.domain.usecases.SaveInvoiceUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
        _state.update { it.copy(meterReading = meterReading) }
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
                    meterStateId = state.value.meterStateId,
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
                                e.printStackTrace()
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
                e.printStackTrace()
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
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }

            try {
                coroutineScope {
                    val routeDeferred = async {
                        var res: Result<Pair<EmployeeRoute?, EmployeeRouteConfig?>>? = null
                        getEmployeeRouteByIdUseCase(employeeRouteId = employeeRouteId).collect { res = it }
                        res ?: Result.Exception(IllegalStateException("Sin resultado de ruta"))
                    }
                    val readingFormDataDeferred = async {
                        var res: Result<ReadingFormData?>? = null
                        getReadingFormDataByEmployeeRouteId(employeeRouteId = employeeRouteId).collect { res = it }
                        res ?: Result.Exception(IllegalStateException("Sin resultado del formulario"))
                    }

                    val routeResult = routeDeferred.await()
                    val readingFormDataResult = readingFormDataDeferred.await()

                    when {
                        routeResult is Result.Error -> _state.update {
                            it.copy(isLoading = false, error = routeResult.error.message)
                        }
                        routeResult is Result.Exception -> _state.update {
                            it.copy(isLoading = false, error = routeResult.error.message)
                        }
                        readingFormDataResult is Result.Error -> _state.update {
                            it.copy(isLoading = false, error = readingFormDataResult.error.message)
                        }
                        readingFormDataResult is Result.Exception -> _state.update {
                            it.copy(isLoading = false, error = readingFormDataResult.error.message)
                        }
                        routeResult is Result.Success && readingFormDataResult is Result.Success -> {
                            val route = routeResult.data.first
                            val config = routeResult.data.second
                            val readingFormData = readingFormDataResult.data
                            val initialMeterStateId = route?.contador?.idEstadoContador
                            val meterStateId = readingFormData?.meterStateId ?: initialMeterStateId
                            val meterReading = getMeterReadingForState(
                                meterStateId = meterStateId,
                                projectedReading = route?.contador?.lecturaProyectada,
                                fallbackReading = readingFormData?.meterReading.orEmpty()
                            )

                            _state.update {
                                it.copy(
                                    route = route,
                                    config = config,
                                    serial = readingFormData?.serial ?: route?.contador?.serial ?: "",
                                    readingFormData = readingFormData,
                                    meterStateId = meterStateId,
                                    meterReading = meterReading,
                                    abnormalConsumption = readingFormData?.abnormalConsumption,
                                    observations = readingFormData?.observations ?: "",
                                    isLoading = false
                                )
                            }
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
}