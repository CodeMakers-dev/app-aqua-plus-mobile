package com.codemakers.aquaplus.ui.modules.home.features.form

import androidx.lifecycle.viewModelScope
import com.codemakers.aquaplus.base.BaseViewModel
import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.usecases.CreateOrUpdateReadingFormData
import com.codemakers.aquaplus.domain.usecases.GetEmployeeRouteByIdUseCase
import com.codemakers.aquaplus.domain.usecases.GetReadingFormDataByEmployeeRouteId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
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
    val state = _state.onStart {
        init()
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), initialState
    )

    //Init

    fun init() {
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
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
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
        }
    }

    //Validations

    private fun loadDataFromId(employeeRouteId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
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
        }
    }

    private fun loadReadingFormData(employeeRouteId: Int) {
        viewModelScope.launch {
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
        }
    }
}