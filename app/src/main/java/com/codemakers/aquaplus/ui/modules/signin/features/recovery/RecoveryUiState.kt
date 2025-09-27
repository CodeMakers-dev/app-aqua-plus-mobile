package com.codemakers.aquaplus.ui.modules.signin.features.recovery

data class RecoveryUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val emailError: String? = null,
    val emailSuccess: String? = null,
    val isEmailInvalid: Boolean = false,
    val emailRequired: Boolean = false,
)
