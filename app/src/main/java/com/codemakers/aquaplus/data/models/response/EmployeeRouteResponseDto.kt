package com.codemakers.aquaplus.data.models.response

import com.codemakers.aquaplus.domain.models.Contador
import com.codemakers.aquaplus.domain.models.DeudaAbonoSaldo
import com.codemakers.aquaplus.domain.models.DeudaCliente
import com.codemakers.aquaplus.domain.models.Direccion
import com.codemakers.aquaplus.domain.models.EmployeeRoute
import com.codemakers.aquaplus.domain.models.Empresa
import com.codemakers.aquaplus.domain.models.HistoricoConsumo
import com.codemakers.aquaplus.domain.models.PersonaCliente
import com.codemakers.aquaplus.domain.models.TarifaContador
import com.codemakers.aquaplus.domain.models.UltimaFactura
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeRouteDto(
    val data: List<EmployeeRouteResponseDto>
)

@Serializable
data class EmployeeRouteResponseDto(
    val empresaClienteContador: EmpresaClienteContadorDto
)

@Serializable
data class EmpresaClienteContadorDto(
    val id: Int,
    val empresa: EmpresaDto,
    val contador: ContadorDto,
    val codFactura: String,
    val codConvenio: String?,
    val ultimaFactura: UltimaFacturaDto?,
    val personaCliente: PersonaClienteDto,
)

@Serializable
data class UltimaFacturaDto(
    val fecha: String?,
    val codigo: String?,
    val precio: Double?,
    val lectura: Double?
)


@Serializable
data class EmpresaDto(
    val id: Int,
    val nit: String,
    val activo: Boolean?,
    val codigo: String,
    val nombre: String,
    val direccion: DireccionDto?,
    val logoEmpresa: GenericEmpresaDto? = null,
    val puntosPago: List<GenericEmpresaDto>? = null,
    val codigoQr: GenericEmpresaDto? = null,
    val correoEmpresa: String? = null,
    val telefonoEmpresa: String? = null,
    val piePagina: String? = null,
    val avisoFactura: String? = null,
)

@Serializable
data class ContadorDto(
    val id: Int,
    val nuid: Long?,
    val serial: String?,
    val digitos: Int?,
    val estrato: Int?,
    val idTipoUso: Int?,
    val matricula: String?,
    val nombreTipoUso: String?,
    val ultimaLectura: Double?,
    val idTipoContador: Int?,
    val tarifaContador: List<TarifaContadorDto>?,
    val deudaAbonoSaldo: DeudaAbonoSaldoDto,
    val promedioConsumo: Double?,
    val fechaInstalacion: String?,
    val historicoConsumo: List<HistoricoConsumoDto>?,
    val idEstadoContador: Int?,
    val lecturaProyectada: Double?,
    val nombreTipoContador: String,
    val nombreEstadoContador: String?
)

@Serializable
data class TarifaContadorDto(
    val aplica: Boolean?,
    val idTipoTarifa: Int?
)

@Serializable
data class DeudaClienteDto(
    val nuevoSaldo: Double?,
    val valorCuota: Double?,
    val idTipoDeuda: Int?,
    val numeroCuotas: Int?,
    val codigoTipoDeuda: String?,
    val nombreTipoDeuda: String?,
    val abonosRealizados: Int?,
    val cuotasCanceladas: Int?,
    val cuotasPendientes: Int?
)

@Serializable
data class DeudaAbonoSaldoDto(
    val deudaTotal: Double,
    val moraActual: Double,
    val abonosTotal: Double
)

@Serializable
data class HistoricoConsumoDto(
    val mes: String?,
    val consumo: Int?,
    val precio: Double?,
    val fechaLectura: String?
)


@Serializable
data class PersonaClienteDto(
    val id: Int,
    val codigo: String?,
    val direccion: DireccionDto,
    val deudaCliente: List<DeudaClienteDto>?,
    val discapacidad: Boolean?,
    val numeroCedula: String,
    val primerNombre: String,
    val segundoNombre: String?,
    val primerApellido: String,
    val segundoApellido: String?
)

@Serializable
data class DireccionDto(
    val ciudad: String?,
    val descripcion: String?,
    val departamento: String?,
    val corregimiento: String?,
)

fun DireccionDto.toDomain(): Direccion = Direccion(
    ciudad = ciudad,
    descripcion = descripcion,
    departamento = departamento,
    corregimiento = corregimiento
)

fun EmpresaClienteContadorDto.toDomain(): EmployeeRoute =
    EmployeeRoute(
        id = id,
        empresa = empresa.toDomain(),
        contador = contador.toDomain(),
        codFactura = codFactura,
        codConvenio = codConvenio,
        ultimaFactura = ultimaFactura?.toDomain(),
        personaCliente = personaCliente.toDomain(),
    )

fun UltimaFacturaDto.toDomain(): UltimaFactura = UltimaFactura(
    fecha = fecha,
    codigo = codigo,
    precio = precio,
    lectura = lectura
)


fun EmpresaDto.toDomain(): Empresa = Empresa(
    id = id,
    nit = nit,
    activo = activo,
    codigo = codigo,
    nombre = nombre,
    direccion = direccion?.toDomain(),
    logoEmpresa = logoEmpresa?.toDomain(),
    puntosPago = puntosPago?.map { it.toDomain() },
    codigoQr = codigoQr?.toDomain(),
    correoEmpresa = correoEmpresa,
    telefonoEmpresa = telefonoEmpresa,
    piePagina = piePagina,
    avisoFactura = avisoFactura
)

fun ContadorDto.toDomain(): Contador = Contador(
    id = id,
    nuid = nuid,
    serial = serial,
    digitos = digitos,
    estrato = estrato,
    idTipoUso = idTipoUso,
    matricula = matricula,
    nombreTipoUso = nombreTipoUso,
    ultimaLectura = ultimaLectura,
    idTipoContador = idTipoContador,
    tarifaContador = tarifaContador?.map { it.toDomain() },
    deudaAbonoSaldo = deudaAbonoSaldo.toDomain(),
    promedioConsumo = promedioConsumo,
    fechaInstalacion = fechaInstalacion,
    historicoConsumo = historicoConsumo?.map { it.toDomain() },
    idEstadoContador = idEstadoContador,
    lecturaProyectada = lecturaProyectada,
    nombreTipoContador = nombreTipoContador,
    nombreEstadoContador = nombreEstadoContador
)

fun TarifaContadorDto.toDomain(): TarifaContador = TarifaContador(
    aplica = aplica,
    idTipoTarifa = idTipoTarifa
)

fun DeudaClienteDto.toDomain(): DeudaCliente = DeudaCliente(
    nuevoSaldo = nuevoSaldo,
    valorCuota = valorCuota,
    idTipoDeuda = idTipoDeuda,
    numeroCuotas = numeroCuotas,
    codigoTipoDeuda = codigoTipoDeuda,
    nombreTipoDeuda = nombreTipoDeuda,
    abonosRealizados = abonosRealizados,
    cuotasCanceladas = cuotasCanceladas,
    cuotasPendientes = cuotasPendientes
)

fun DeudaAbonoSaldoDto.toDomain(): DeudaAbonoSaldo = DeudaAbonoSaldo(
    deudaTotal = deudaTotal,
    moraActual = moraActual,
    abonosTotal = abonosTotal,
)

fun HistoricoConsumoDto.toDomain(): HistoricoConsumo = HistoricoConsumo(
    mes = mes,
    consumo = consumo,
    precio = precio,
    fechaLectura = fechaLectura
)


fun PersonaClienteDto.toDomain(): PersonaCliente = PersonaCliente(
    id = id,
    codigo = codigo,
    direccion = direccion.toDomain(),
    deudaCliente = deudaCliente?.map { it.toDomain() },
    discapacidad = discapacidad,
    numeroCedula = numeroCedula,
    primerNombre = primerNombre,
    segundoNombre = segundoNombre,
    primerApellido = primerApellido,
    segundoApellido = segundoApellido,
)