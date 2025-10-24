package com.codemakers.aquaplus.ui.modules.home.features.form

import androidx.lifecycle.viewModelScope
import com.codemakers.aquaplus.base.BaseViewModel
import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.usecases.CreateOrUpdateReadingFormData
import com.codemakers.aquaplus.domain.usecases.GetEmployeeRouteByIdUseCase
import com.codemakers.aquaplus.domain.usecases.GetReadingFormDataByEmployeeRouteId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
) : BaseViewModel() {

    private val initialState = ReadingFormUiState(
        employeeRouteId = employeeRouteId,
    )

    private val _state = MutableStateFlow(initialState)
    private var loadDataJob: Job? = null
    private var saveDataJob: Job? = null
    
    val state = _state.stateIn(
        viewModelScope, SharingStarted.Lazily, initialState
    )

    private var isInitialized = false

    init {
        loadDataFromId(state.value.employeeRouteId)
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

    fun onObservationsChange(observations: String) {
        _state.update { it.copy(observations = observations) }
    }

    fun onSave() {
        saveDataJob?.cancel()
        
        saveDataJob = viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            
            try {
                createOrUpdateReadingFormData(
                    employeeRouteId = state.value.employeeRouteId,
                    meterReading = state.value.meterReading,
                    abnormalConsumption = state.value.abnormalConsumption,
                    observations = state.value.observations,
                    readingFormDataId = state.value.readingFormDataId,
                    date = LocalDate.now(),
                ).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _state.update {
                                it.copy(
                                    isCreatedOrUpdatedSuccess = true,
                                    readingFormData = result.data,
                                )
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
        if (isInitialized) return
        isInitialized = true
        
        loadDataJob?.cancel()
        
        loadDataJob = viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            
            try {
                getEmployeeRouteByIdUseCase(employeeRouteId = employeeRouteId).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _state.update {
                                it.copy(
                                    route = result.data.first,
                                    serial = result.data.first?.contador?.serial ?: "",
                                    config = result.data.second,
                                )
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
                        is Result.Success -> _state.update {
                            it.copy(
                                readingFormData = result.data,
                                meterReading = result.data?.meterReading ?: "",
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
    
    override fun onCleared() {
        super.onCleared()
        loadDataJob?.cancel()
        saveDataJob?.cancel()
    }
}