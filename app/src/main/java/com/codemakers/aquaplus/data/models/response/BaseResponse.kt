package com.codemakers.aquaplus.data.models.response

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val success: Boolean? = null,
    val message: String? = null,
    val code: Int? = null,
    val response: T,
)