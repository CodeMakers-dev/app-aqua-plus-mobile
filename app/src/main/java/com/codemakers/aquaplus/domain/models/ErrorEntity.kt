package com.codemakers.aquaplus.domain.models

data class ErrorEntity(
    val success: Boolean? = null,
    val message: String? = null,
    val code: Int? = null,
)