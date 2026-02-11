package com.codemakers.aquaplus.ui.modules.signin.features.login

data class LoginUiState(
    val isLoading: Boolean = false,
    val username: String = "juan",
    val password: String = "ADMIN1.#12",
    val showPass: Boolean = false,
    val usernameRequired: Boolean = false,
    val passwordRequired: Boolean = false,
    val errorLogin: String? = null,
    val navigateToHome: Boolean = false,
    val navigateToLogin: Boolean = false,
)
