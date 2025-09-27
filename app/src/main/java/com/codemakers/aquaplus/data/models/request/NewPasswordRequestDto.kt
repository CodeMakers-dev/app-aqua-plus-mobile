package com.codemakers.aquaplus.data.models.request

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class NewPasswordRequestDto(
    @SerializedName("contrasena") val password: String,
)