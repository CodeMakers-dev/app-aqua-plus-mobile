package com.codemakers.aquaplus.domain.models

import com.codemakers.aquaplus.data.common.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class ReadingFormData(
    val id: Long,
    val personId: Int,
    val employeeRouteId: Int,
    val meterReading: String,
    val abnormalConsumption: Boolean?,
    val observations: String,
    @Serializable(with = LocalDateSerializer::class) val date: LocalDate,
    val isSynced: Boolean,
    val serial: String?,
    val meterStateId: Int?,
)