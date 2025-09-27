package com.codemakers.aquaplus.data.models.request

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class InvoiceRequestDto(
    @SerializedName("codigo") val code: String,
    @SerializedName("idEmpresaClienteContador") val idContador: Int,
    @SerializedName("precio") val precio: Double,
    @SerializedName("fechaEmision") val fechaEmision: String,
    @SerializedName("usuarioCreacion") val usuarioCreacion: String,
    @SerializedName("codEstado") val codEstado: String,
    @SerializedName("lectura") val lectura: ReadRequestDto
)

@Serializable
data class ReadRequestDto(
    @SerializedName("lectura") val meterReading: String,
    @SerializedName("descripcion") val description: String,
    @SerializedName("consumoAnormal") val abnormalConsumption: Boolean,
)