package com.codemakers.aquaplus.ui.modules.home.features.profile

import androidx.lifecycle.viewModelScope
import com.codemakers.aquaplus.base.BaseViewModel
import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.usecases.CleanProfileUseCase
import com.codemakers.aquaplus.domain.usecases.GetProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val cleanProfileUseCase: CleanProfileUseCase,
) : BaseViewModel() {

    private val initialState = ProfileUiState()

    private val _state = MutableStateFlow(initialState)
    val state = _state.onStart {
        init()
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), initialState
    )

    //Init

    fun init() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getProfileUseCase().collect { result ->
                when (result) {
                    is Result.Success -> _state.update {
                        it.copy(
                            profile = result.data,
                            isLoading = false,
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

    //Actions

    fun cleanError() {
        _state.update { it.copy(error = null) }
    }

    fun onLogout() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            cleanProfileUseCase().collect {
                _state.update { it.copy(isLoading = false, navigateToLogin = true) }
            }
        }
    }
}