package com.codemakers.aquaplus.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Invoice(
    val updated: Int? = null,
    val inserted: Int? = null,
    val updatedIds: List<Int>? = null,
    val insertedIds: List<Int>? = null,
) {

    val allIds: List<Int>
        get() = updatedIds.orEmpty() + insertedIds.orEmpty()
}