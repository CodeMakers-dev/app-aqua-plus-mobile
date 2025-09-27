package com.codemakers.aquaplus.ui.modules.signin.features.newpassword

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.codemakers.aquaplus.base.BaseViewModel
import com.codemakers.aquaplus.data.repository.TOKEN
import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.usecases.UpdatePasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewPasswordViewModel(
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _state = MutableStateFlow(NewPasswordUiState())
    val state: StateFlow<NewPasswordUiState> = _state.asStateFlow()

    private val token: String? = savedStateHandle.get<String>(TOKEN)

    fun onNewPassword1Change(password: String) {
        _state.update {
            it.copy(
                newPassword1 = password,
                passwordError = null,
                passwordRequired = false,
                passwordNotMatch = false
            )
        }
    }

    fun onNewPassword2Change(password: String) {
        _state.update {
            it.copy(
                newPassword2 = password,
                passwordError = null,
                passwordRequired = false,
                passwordNotMatch = false
            )
        }
    }

    //Actions

    fun onChangePasswordClick() {
        _state.update { it.copy(isLoading = true) }
        val pass1 = _state.value.newPassword1
        val pass2 = _state.value.newPassword2

        if (pass1.isBlank() || pass2.isBlank()) {
            _state.update {
                it.copy(
                    passwordRequired = true,
                    isLoading = false
                )
            }
            return
        }

        if (pass1 != pass2) {
            _state.update {
                it.copy(
                    passwordNotMatch = true,
                    isLoading = false
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            updatePasswordUseCase(token = token.orEmpty(), newPassword = pass1).collect { result ->
                when (result) {
                    is Result.Success -> _state.update {
                        it.copy(isLoading = false, navigateToLogin = true)
                    }

                    is Result.Error -> _state.update {
                        it.copy(isLoading = false, passwordError = result.error.message)
                    }

                    is Result.Exception -> _state.update {
                        it.copy(
                            isLoading = false,
                            passwordError = result.error.message
                        )
                    }
                }
            }
        }
    }

    fun cleanNavigateToLogin() {
        _state.update { it.copy(navigateToLogin = false, passwordError = null) }

    }
}