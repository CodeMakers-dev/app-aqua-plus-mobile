package com.codemakers.aquaplus.data.models.response

import com.codemakers.aquaplus.domain.models.Token
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    @SerializedName("token") val token: String
)

fun TokenDto.toDomain() = Token(
    token = token
)