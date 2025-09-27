package com.codemakers.aquaplus.ui.modules.home.features.profile

import com.codemakers.aquaplus.domain.models.Profile

data class ProfileUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val profile: Profile? = null,
    val navigateToLogin: Boolean = false,
)
