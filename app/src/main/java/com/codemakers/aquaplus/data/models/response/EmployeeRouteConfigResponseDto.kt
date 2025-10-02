package com.codemakers.aquaplus.data.models.response

import com.codemakers.aquaplus.domain.models.Catalogos
import com.codemakers.aquaplus.domain.models.Concepto
import com.codemakers.aquaplus.domain.models.Config
import com.codemakers.aquaplus.domain.models.EmployeeRouteConfig
import com.codemakers.aquaplus.domain.models.GenericEmpresa
import com.codemakers.aquaplus.domain.models.TarifaEmpresa
import com.codemakers.aquaplus.domain.models.TipoConcepto
import com.codemakers.aquaplus.domain.models.TipoTarifa
import com.codemakers.aquaplus.domain.models.ValorEstrato
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeRouteConfigDto(
    @SerializedName("data") val config: ConfigDto? = null,
    val catalogos: CatalogosDto? = null
)

@Serializable
data class ConfigDto(
    val empresa: EmpresaDto? = null,
    val tarifasEmpresa: List<TarifaEmpresaDto>? = null
)

@Serializable
data class TarifaEmpresaDto(
    val id: Int? = null,
    val empresa: Int? = null,
    val conceptos: List<ConceptoDto>? = null,
    val tipoTarifa: TipoTarifaDto? = null
)

@Serializable
data class ConceptoDto(
    val id: Int? = null,
    val valor: Double? = null,
    val tipoConcepto: TipoConceptoDto? = null,
    val indCalcularMc: Boolean? = null,
    val valoresEstrato: List<ValorEstratoDto>? = null
)

@Serializable
data class TipoConceptoDto(
    val id: Int? = null,
    val codigo: String? = null,
    val descripcion: String? = null
)

@Serializable
data class ValorEstratoDto(
    val id: Int? = null,
    val valor: Double? = null,
    val estrato: Int? = null
)

@Serializable
data class TipoTarifaDto(
    val id: Int? = null,
    val codigo: String? = null,
    val nombre: String? = null,
    val descripcion: String? = null
)

@Serializable
data class CatalogosDto(
    val tiposTarifa: List<TipoTarifaDto>? = null,
    val tiposConcepto: List<TipoConceptoDto>? = null
)

@Serializable
data class GenericEmpresaDto(
    val nombre: String? = null,
    val imagen: String? = null
)

fun EmployeeRouteConfigDto.toDomain(): EmployeeRouteConfig = EmployeeRouteConfig(
    config = config?.toDomain(),
    catalogos = catalogos?.toDomain()
)

fun ConfigDto.toDomain(): Config = Config(
    empresa = empresa?.toDomain(),
    tarifasEmpresa = tarifasEmpresa?.map { it.toDomain() }
)

fun TarifaEmpresaDto.toDomain(): TarifaEmpresa = TarifaEmpresa(
    id = id,
    empresa = empresa,
    conceptos = conceptos?.map { it.toDomain() },
    tipoTarifa = tipoTarifa?.toDomain()
)

fun ConceptoDto.toDomain(): Concepto = Concepto(
    id = id,
    valor = valor,
    tipoConcepto = tipoConcepto?.toDomain(),
    indCalcularMc = indCalcularMc,
    valoresEstrato = valoresEstrato?.map { it.toDomain() }
)

fun TipoConceptoDto.toDomain(): TipoConcepto = TipoConcepto(
    id = id,
    codigo = codigo,
    descripcion = descripcion,
)

fun ValorEstratoDto.toDomain(): ValorEstrato = ValorEstrato(
    id = id,
    valor = valor,
    estrato = estrato
)

fun TipoTarifaDto.toDomain(): TipoTarifa = TipoTarifa(
    id = id,
    codigo = codigo,
    nombre = nombre,
    descripcion = descripcion
)

fun CatalogosDto.toDomain(): Catalogos = Catalogos(
    tiposTarifa = tiposTarifa?.map { it.toDomain() },
    tiposConcepto = tiposConcepto?.map { it.toDomain() }
)

fun GenericEmpresaDto.toDomain(): GenericEmpresa = GenericEmpresa(
    nombre = nombre,
    imagen = imagen
)