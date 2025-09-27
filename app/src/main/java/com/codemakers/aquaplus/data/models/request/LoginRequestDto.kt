package com.codemakers.aquaplus.data.models.request

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    @SerializedName("nombre") val username: String,
    @SerializedName("contrasena") val password: String
)