package com.codemakers.aquaplus.ui.modules.signin.features.recovery

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.codemakers.aquaplus.base.BaseViewModel
import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.usecases.RecoveryPasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecoveryViewModel(
    private val recoveryPasswordUseCase: RecoveryPasswordUseCase
) : BaseViewModel() {

    private val initialState = RecoveryUiState()

    private val _state = MutableStateFlow(initialState)
    val state = _state.onStart {
        init()
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), initialState
    )

    fun init() {}

    fun onEmailChange(email: String) {
        _state.update {
            it.copy(
                email = email,
                emailError = null,
                isEmailInvalid = false,
                emailRequired = false
            )
        }
    }

    fun onContinueClick() {
        val email = _state.value.email
        if (email.isBlank()) {
            _state.update {
                it.copy(emailRequired = true)
            }
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _state.update {
                it.copy(isEmailInvalid = true)
            }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            recoveryPasswordUseCase(email).collect { result ->
                when (result) {
                    is Result.Success -> _state.update {
                        it.copy(
                            isLoading = false,
                            emailError = null,
                            emailSuccess = result.data
                        )
                    }

                    is Result.Error -> _state.update {
                        it.copy(
                            isLoading = false,
                            emailError = result.error.message
                        )
                    }

                    is Result.Exception -> _state.update {
                        it.copy(
                            isLoading = false,
                            emailError = result.error.message
                        )
                    }
                }
            }
        }
    }
}
