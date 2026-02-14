package com.codemakers.aquaplus.ui.modules.signin.features.splash

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.codemakers.aquaplus.base.BaseViewModel
import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.usecases.GetProfileUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(
    private val getProfileUseCase: GetProfileUseCase,
) : BaseViewModel() {

    private val initialState = SplashUiState()

    private val _state = MutableStateFlow(initialState)
    val state = _state.onStart {
        init()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        initialState
    )

    //Init

    fun init() {
        viewModelScope.launch {
            delay(1000)
            getProfileUseCase().collect {
                Log.d("SplashViewModel", "getProfileUseCase: $it")
                val isLogged = (it as? Result.Success)?.data != null
                _state.update { state ->
                    state.copy(
                        navigateToLogin = !isLogged,
                        navigateToHome = isLogged,
                    )
                }
            }
        }
    }

    //Actions

    fun cleanNavigateToLogin() {
        _state.update { state ->
            state.copy(
                navigateToLogin = false,
            )
        }
    }

    fun cleanNavigateToHome() {
        _state.update { state ->
            state.copy(
                navigateToHome = false,
            )
        }
    }

    //Validations
}