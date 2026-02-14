package com.codemakers.aquaplus.ui.modules.home.features.route

import androidx.lifecycle.viewModelScope
import com.codemakers.aquaplus.base.BaseViewModel
import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.usecases.GetAllEmployeeRouteUseCase
import com.codemakers.aquaplus.domain.usecases.GetAllReadingFormDataUseCase
import com.codemakers.aquaplus.domain.usecases.LoadAllEmployeeRouteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RouteViewModel(
    private val getAllEmployeeRouteUseCase: GetAllEmployeeRouteUseCase,
    private val loadAllEmployeeRouteUseCase: LoadAllEmployeeRouteUseCase,
    private val getAllReadingFormDataUseCase: GetAllReadingFormDataUseCase,
) : BaseViewModel() {

    private val initialState = RouteUiState()

    private val _state = MutableStateFlow(initialState)
    private val _searchQuery = MutableStateFlow("")

    val state = _state.stateIn(
        viewModelScope, SharingStarted.Lazily, initialState
    )

    init {
        loadAllEmployeeRoutes()
        loadAllReadingFormDataRoutes() // Observe reading form data continuously
        observeSearchQuery()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300) // Espera 300ms después del último cambio
                .distinctUntilChanged()
                .collect { search ->
                    performSearch(search)
                }
        }
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
                                isLoading = false,
                            )
                        }
                    }

                    is Result.Error -> _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Error3" +result.error.message,
                        )
                    }

                    is Result.Exception -> _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Error4" +result.error.message,
                        )
                    }
                }
            }
        }
    }

    fun onSearchChange(search: String) {
        _state.update { it.copy(search = search) }
        _searchQuery.value = search
    }

    private fun performSearch(search: String) {
        val routesSearched = if (search.isNotBlank()) {
            _state.value.allRoutes?.filter {
                val route = it
                route.personaCliente?.direccion?.descripcion?.lowercase()
                    ?.contains(search.lowercase()) == true ||
                        route.personaCliente?.primerNombre.orEmpty().lowercase()
                            .contains(search.lowercase()) ||
                        route.personaCliente?.primerApellido.orEmpty().lowercase()
                            .contains(search.lowercase()) ||
                        route.personaCliente?.numeroCedula.orEmpty().contains(search) ||
                        route.contador?.serial.orEmpty().lowercase().contains(search.lowercase())
            }
        } else {
            _state.value.allRoutes
        }
        _state.update { it.copy(routes = routesSearched.orEmpty()) }
    }

    fun cleanError() {
        _state.update { it.copy(error = null) }
    }

    //Validations

    private fun loadAllEmployeeRoutes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val result = getAllEmployeeRouteUseCase()
                when (result) {
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                allRoutes = result.data,
                                routes = result.data,
                                search = "",
                                isLoading = false,
                            )
                        }
                    }

                    is Result.Error -> _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Error1" + result.error.message,
                        )
                    }

                    is Result.Exception -> _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Error2" + result.error.message,
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar rutas: ${e.message}",
                    )
                }
            }
        }
    }

    private fun loadAllReadingFormDataRoutes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getAllReadingFormDataUseCase().collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _state.update {
                                it.copy(isLoading = false, allData = result.data)
                            }
                        }

                        else -> _state.update {
                            it.copy(isLoading = false)
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }
}