package com.codemakers.aquaplus.ui.modules.signin.features.login

import androidx.lifecycle.viewModelScope
import com.codemakers.aquaplus.base.BaseViewModel
import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.usecases.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : BaseViewModel() {

    private val initialState = LoginUiState()

    private val _state = MutableStateFlow(initialState)
    val state = _state.onStart {
        init()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        initialState
    )

    //Init

    fun init() {}

    //Actions

    fun onUserValueChange(user: String) {
        _state.update { it.copy(username = user, usernameRequired = false) }
    }

    fun onPassValueChange(pass: String) {
        _state.update { it.copy(password = pass, passwordRequired = false) }
    }

    fun onChangePassVisibility() {
        _state.update { it.copy(showPass = !it.showPass) }
    }

    fun onLogin() {
        if (validateForm()) {
            callToLogin()
        }
    }

    fun cleanLoginError() {
        _state.update { it.copy(errorLogin = null) }
    }

    //Validations

    private fun validateForm(): Boolean {
        if (_state.value.username.isBlank()) {
            _state.update { it.copy(usernameRequired = true) }
            return false
        }

        if (_state.value.password.isBlank()) {
            _state.update { it.copy(passwordRequired = true) }
            return false
        }

        return true
    }

    private fun callToLogin() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            loginUseCase(
                username = _state.value.username,
                password = _state.value.password,
            ).collect { result ->
                when (result) {
                    is Result.Success -> _state.update {
                        it.copy(
                            isLoading = false,
                            navigateToHome = true
                        )
                    }

                    is Result.Error -> _state.update {
                        it.copy(
                            isLoading = false,
                            errorLogin = result.error.message
                        )
                    }

                    is Result.Exception -> _state.update {
                        it.copy(
                            isLoading = false,
                            errorLogin = result.error.message
                        )
                    }
                }
            }
        }
    }
}
