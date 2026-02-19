package com.codemakers.aquaplus.data.models.request

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class InvoiceRequestDto(
    @SerializedName("codigo") val code: String,
    @SerializedName("idEmpresaClienteContador") val idContador: Int,
    @SerializedName("precio") val precio: Double,
    @SerializedName("idEstadoMedidor") val meterStateId: Int,
    @SerializedName("serial") val serial: String,
    @SerializedName("fechaEmision") val fechaEmision: String,
    @SerializedName("usuarioCreacion") val usuarioCreacion: String,
    @SerializedName("lectura") val lectura: ReadRequestDto
)

@Serializable
data class ReadRequestDto(
    @SerializedName("lectura") val meterReading: String,
    @SerializedName("descripcion") val description: String,
    @SerializedName("consumoAnormal") val abnormalConsumption: Boolean,
)