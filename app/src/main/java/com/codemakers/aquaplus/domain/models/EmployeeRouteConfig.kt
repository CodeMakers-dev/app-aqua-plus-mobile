package com.codemakers.aquaplus.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeRouteConfig(
    val config: Config? = null,
    val catalogos: Catalogos? = null
)

@Serializable
data class Config(
    val empresa: Empresa? = null,
    val tarifasEmpresa: List<TarifaEmpresa>? = null
)

@Serializable
data class TarifaEmpresa(
    val id: Int? = null,
    val empresa: Int? = null,
    val conceptos: List<Concepto>? = null,
    val tipoTarifa: TipoTarifa? = null
)

@Serializable
data class Concepto(
    val id: Int? = null,
    val valor: Double? = null,
    val tipoConcepto: TipoConcepto? = null,
    val indCalcularMc: Boolean? = null,
    val valoresEstrato: List<ValorEstrato>? = null
)

@Serializable
data class TipoConcepto(
    val id: Int? = null,
    val codigo: String? = null,
    val descripcion: String? = null
)

@Serializable
data class ValorEstrato(
    val id: Int? = null,
    val valor: Double? = null,
    val estrato: Int? = null
)

@Serializable
data class TipoTarifa(
    val id: Int? = null,
    val codigo: String? = null,
    val nombre: String? = null,
    val descripcion: String? = null
)

@Serializable
data class Catalogos(
    val tiposTarifa: List<TipoTarifa>? = null,
    val tiposConcepto: List<TipoConcepto>? = null
)