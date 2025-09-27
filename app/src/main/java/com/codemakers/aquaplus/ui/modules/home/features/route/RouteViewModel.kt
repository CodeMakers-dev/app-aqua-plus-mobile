package com.codemakers.aquaplus.ui.modules.home.features.route

import androidx.lifecycle.viewModelScope
import com.codemakers.aquaplus.base.BaseViewModel
import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.EmployeeRoute
import com.codemakers.aquaplus.domain.usecases.GetAllEmployeeRouteUseCase
import com.codemakers.aquaplus.domain.usecases.GetAllReadingFormDataUseCase
import com.codemakers.aquaplus.domain.usecases.GetEmployeeRouteConfigAndReadingFormDataByIdUseCase
import com.codemakers.aquaplus.domain.usecases.LoadAllEmployeeRouteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RouteViewModel(
    private val getAllEmployeeRouteUseCase: GetAllEmployeeRouteUseCase,
    private val loadAllEmployeeRouteUseCase: LoadAllEmployeeRouteUseCase,
    private val getEmployeeRouteConfigAndReadingFormDataByIdUseCase: GetEmployeeRouteConfigAndReadingFormDataByIdUseCase,
    private val getAllReadingFormDataUseCase: GetAllReadingFormDataUseCase,
) : BaseViewModel() {

    private val initialState = RouteUiState()

    private val _state = MutableStateFlow(initialState)
    val state = _state.onStart {
        init()
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), initialState
    )

    //Init

    fun init() {
        loadAllEmployeeRoutes()
    }

    //Actions

    fun loadAllRoutes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            loadAllEmployeeRouteUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                allRoutes = result.data,
                                routes = result.data,
                                search = "",
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

    fun onSearchChange(search: String) {
        val routesSearched = if (search.isNotBlank()) {
            _state.value.allRoutes?.filter {
                it.toString().lowercase().contains(search.lowercase())
            }
        } else {
            _state.value.allRoutes
        }
        _state.update { it.copy(search = search, routes = routesSearched.orEmpty()) }
    }

    fun loadReadingFormData(route: EmployeeRoute) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val employeeRouteId = route.id
            val empresaId = route.empresa.id ?: 0
            getEmployeeRouteConfigAndReadingFormDataByIdUseCase(
                employeeRouteId = employeeRouteId,
                empresaId = empresaId,
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        val data = result.data.second
                        if (data == null) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = "No se encontraron datos para la ruta seleccionada",
                                )
                            }
                            return@collect
                        }
                        _state.update {
                            it.copy(
                                isLoading = false,
                                route = route,
                                config = result.data.first,
                                data = result.data.second,
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

    fun cleanError() {
        _state.update { it.copy(error = null) }
    }
    fun cleanReadingFormData() {
        _state.update { it.copy(route = null, config = null, data = null) }
    }

    //Validations

    private fun loadAllEmployeeRoutes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getAllEmployeeRouteUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                allRoutes = result.data,
                                routes = result.data,
                                search = "",
                            )
                        }
                        loadAllReadingFormDataRoutes()
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

    private fun loadAllReadingFormDataRoutes() {
        viewModelScope.launch {
            getAllReadingFormDataUseCase().collect { result ->
                when (result) {
                    is Result.Success -> _state.update {
                        it.copy(isLoading = false, allData = result.data)
                    }

                    else -> _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                }
            }
        }
    }
}