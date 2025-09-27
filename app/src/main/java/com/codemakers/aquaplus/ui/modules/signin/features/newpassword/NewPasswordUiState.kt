package com.codemakers.aquaplus.ui.modules.signin.features.newpassword

data class NewPasswordUiState(
    val newPassword1: String = "",
    val newPassword2: String = "",
    val passwordRequired: Boolean = false,
    val passwordNotMatch: Boolean = false,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val navigateToLogin: Boolean = false
)