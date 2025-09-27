package com.codemakers.aquaplus.domain.models

import com.codemakers.aquaplus.data.models.request.InvoiceRequestDto
import com.codemakers.aquaplus.data.models.request.ReadRequestDto

data class InvoiceRequest(
    val code: String,
    val idContador: Int,
    val precio: Double,
    val fechaEmision: String,
    val usuarioCreacion: String,
    val codEstado: String,
    val lectura: ReadRequest,
)

data class ReadRequest(
    val meterReading: String,
    val description: String,
    val abnormalConsumption: Boolean,
)

fun InvoiceRequest.toData() = InvoiceRequestDto(
    code = code,
    idContador = idContador,
    precio = precio,
    fechaEmision = fechaEmision,
    usuarioCreacion = usuarioCreacion,
    codEstado = codEstado,
    lectura = lectura.toData()
)

fun ReadRequest.toData() = ReadRequestDto(
    meterReading = meterReading,
    description = description,
    abnormalConsumption = abnormalConsumption
)