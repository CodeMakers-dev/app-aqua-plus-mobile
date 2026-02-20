package com.codemakers.aquaplus.data.models.response

import com.codemakers.aquaplus.domain.models.Concepto
import com.codemakers.aquaplus.domain.models.Config
import com.codemakers.aquaplus.domain.models.EmployeeRouteConfig
import com.codemakers.aquaplus.domain.models.EstadoMedidor
import com.codemakers.aquaplus.domain.models.GenericEmpresa
import com.codemakers.aquaplus.domain.models.ParametrosEmpresa
import com.codemakers.aquaplus.domain.models.RangoConsumo
import com.codemakers.aquaplus.domain.models.TarifaEmpresa
import com.codemakers.aquaplus.domain.models.TipoConcepto
import com.codemakers.aquaplus.domain.models.TipoTarifa
import com.codemakers.aquaplus.domain.models.TipoUso
import com.codemakers.aquaplus.domain.models.TipoUsoWrapper
import com.codemakers.aquaplus.domain.models.ValorEstrato
import com.codemakers.aquaplus.domain.models.ValorMc
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeRouteConfigDto(
    @SerializedName("data") val config: ConfigDto? = null,
)

@Serializable
data class ConfigDto(
    val empresa: EmpresaDto? = null,
    val tipoUso: TipoUsoWrapperDto? = null,
    val estadosMedidor: List<EstadoMedidorDto>? = null,
    val tarifasEmpresa: List<TarifaEmpresaDto>? = null,
    val parametrosEmpresa: ParametrosEmpresaDto? = null
)

@Serializable
data class TarifaEmpresaDto(
    val id: Int? = null,
    val empresa: Int? = null,
    val valorMc: List<ValorMcDto>? = null,
    val conceptos: List<ConceptoDto>? = null,
    val tipoTarifa: TipoTarifaDto? = null,
    val rangoConsumo: RangoConsumoDto? = null
)

@Serializable
data class RangoConsumoDto(
    val consuBasico: String? = null,
    val consuSuntuario: String? = null,
    val consuComplementario: String? = null
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
data class TipoUsoWrapperDto(
    val data: List<TipoUsoDto>? = null
)

@Serializable
data class TipoUsoDto(
    val id: Int? = null,
    val activo: Boolean? = null,
    val codigo: String? = null,
    val nombre: String? = null,
    val descripcion: String? = null
)

@Serializable
data class EstadoMedidorDto(
    val id: Int? = null,
    val codigo: String? = null,
    val descripcion: String? = null
)

@Serializable
data class ValorMcDto(
    val id: Int? = null,
    val rango: String? = null,
    val valor: Double? = null,
    val codigo: String? = null,
    val nombre: String? = null,
    val estrato: Int? = null,
    val tipoUso: TipoUsoDto? = null
)

@Serializable
data class ParametrosEmpresaDto(
    val consuBasico: String? = null,
    val diasVencida: String? = null,
    val periodosInm: String? = null,
    val periodosVig: String? = null,
    val periodosFact: String? = null,
    val consuSuntuario: String? = null,
    val consuComplementario: String? = null
)

@Serializable
data class GenericEmpresaDto(
    val nombre: String? = null,
    val imagen: String? = null
)

fun EmployeeRouteConfigDto.toDomain(): EmployeeRouteConfig = EmployeeRouteConfig(
    config = config?.toDomain(),
)

fun ConfigDto.toDomain(): Config = Config(
    empresa = empresa?.toDomain(),
    tipoUso = tipoUso?.toDomain(),
    estadosMedidor = estadosMedidor?.map { it.toDomain() },
    tarifasEmpresa = tarifasEmpresa?.map { it.toDomain() },
    parametrosEmpresa = parametrosEmpresa?.toDomain()
)

fun TarifaEmpresaDto.toDomain(): TarifaEmpresa = TarifaEmpresa(
    id = id,
    empresa = empresa,
    valorMc = valorMc?.map { it.toDomain() },
    conceptos = conceptos?.map { it.toDomain() },
    tipoTarifa = tipoTarifa?.toDomain(),
    rangoConsumo = rangoConsumo?.toDomain()
)

fun RangoConsumoDto.toDomain(): RangoConsumo = RangoConsumo(
    consuBasico = consuBasico,
    consuSuntuario = consuSuntuario,
    consuComplementario = consuComplementario
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

fun TipoUsoWrapperDto.toDomain(): TipoUsoWrapper = TipoUsoWrapper(
    data = data?.map { it.toDomain() }
)

fun TipoUsoDto.toDomain(): TipoUso = TipoUso(
    id = id,
    activo = activo,
    codigo = codigo,
    nombre = nombre,
    descripcion = descripcion
)

fun EstadoMedidorDto.toDomain(): EstadoMedidor = EstadoMedidor(
    id = id,
    codigo = codigo,
    descripcion = descripcion
)

fun ValorMcDto.toDomain(): ValorMc = ValorMc(
    id = id,
    rango = rango,
    valor = valor,
    codigo = codigo,
    nombre = nombre,
    estrato = estrato,
    tipoUso = tipoUso?.toDomain()
)

fun ParametrosEmpresaDto.toDomain(): ParametrosEmpresa = ParametrosEmpresa(
    consuBasico = consuBasico,
    diasVencida = diasVencida,
    periodosInm = periodosInm,
    periodosVig = periodosVig,
    periodosFact = periodosFact,
    consuSuntuario = consuSuntuario,
    consuComplementario = consuComplementario
)

fun GenericEmpresaDto.toDomain(): GenericEmpresa = GenericEmpresa(
    nombre = nombre,
    imagen = imagen
)