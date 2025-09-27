package com.codemakers.aquaplus.ui.modules.signin.features.splash

data class SplashUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val navigateToHome: Boolean = false,
    val navigateToLogin: Boolean = false,
)
