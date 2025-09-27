package com.codemakers.aquaplus.domain.models

data class LoginSession(
    val id: Int,
    val username: String,
    val token: String,
    val roleId: Int,
    val personId: Int
)
