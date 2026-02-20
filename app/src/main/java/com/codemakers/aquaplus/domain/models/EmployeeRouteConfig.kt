package com.codemakers.aquaplus.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeRouteConfig(
    val config: Config? = null,
)

@Serializable
data class Config(
    val empresa: Empresa? = null,
    val tipoUso: TipoUsoWrapper? = null,
    val estadosMedidor: List<EstadoMedidor>? = null,
    val tarifasEmpresa: List<TarifaEmpresa>? = null,
    val parametrosEmpresa: ParametrosEmpresa? = null
)

@Serializable
data class TarifaEmpresa(
    val id: Int? = null,
    val empresa: Int? = null,
    val valorMc: List<ValorMc>? = null,
    val conceptos: List<Concepto>? = null,
    val tipoTarifa: TipoTarifa? = null,
    val rangoConsumo: RangoConsumo? = null
)

@Serializable
data class RangoConsumo(
    val consuBasico: String? = null,
    val consuSuntuario: String? = null,
    val consuComplementario: String? = null
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
data class TipoUsoWrapper(
    val data: List<TipoUso>? = null
)

@Serializable
data class TipoUso(
    val id: Int? = null,
    val activo: Boolean? = null,
    val codigo: String? = null,
    val nombre: String? = null,
    val descripcion: String? = null
)

@Serializable
data class EstadoMedidor(
    val id: Int? = null,
    val codigo: String? = null,
    val descripcion: String? = null
)

@Serializable
data class ValorMc(
    val id: Int? = null,
    val rango: String? = null,
    val valor: Double? = null,
    val codigo: String? = null,
    val nombre: String? = null,
    val estrato: Int? = null,
    val tipoUso: TipoUso? = null
)

@Serializable
data class ParametrosEmpresa(
    val consuBasico: String? = null,
    val diasVencida: String? = null,
    val periodosInm: String? = null,
    val periodosVig: String? = null,
    val periodosFact: String? = null,
    val consuSuntuario: String? = null,
    val consuComplementario: String? = null
)