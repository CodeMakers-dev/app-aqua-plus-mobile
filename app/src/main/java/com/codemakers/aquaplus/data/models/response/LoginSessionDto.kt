package com.codemakers.aquaplus.data.models.response

import com.codemakers.aquaplus.domain.models.LoginSession
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class LoginSessionDto(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val username: String,
    @SerializedName("token") val token: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("rolId") val roleId: Int,
    @SerializedName("personaId") val personId: Int
)

fun LoginSessionDto.toDomain() = LoginSession(
    id = id,
    username = username,
    token = token,
    roleId = roleId,
    personId = personId
)