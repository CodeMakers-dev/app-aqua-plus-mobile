package com.codemakers.aquaplus.data.models.response

import com.codemakers.aquaplus.domain.models.InvoiceResponse
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class InvoiceResponseDto(
    @SerializedName("updated") val updated: Int? = null,
    @SerializedName("inserted") val inserted: Int? = null,
    @SerializedName("updatedIds") val updatedIds: List<Int>? = null,
    @SerializedName("insertedIds") val insertedIds: List<Int>? = null,
)

fun InvoiceResponseDto.toDomain(): InvoiceResponse = InvoiceResponse(
    updated = updated,
    inserted = inserted,
    updatedIds = updatedIds,
    insertedIds = insertedIds,
)