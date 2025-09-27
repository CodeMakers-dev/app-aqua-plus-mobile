package com.codemakers.aquaplus.data.models.response

import com.codemakers.aquaplus.domain.models.Contador
import com.codemakers.aquaplus.domain.models.Deuda
import com.codemakers.aquaplus.domain.models.DeudaAbonoSaldo
import com.codemakers.aquaplus.domain.models.DiasFactura
import com.codemakers.aquaplus.domain.models.Direccion
import com.codemakers.aquaplus.domain.models.EmployeeRoute
import com.codemakers.aquaplus.domain.models.Empresa
import com.codemakers.aquaplus.domain.models.HistoricoConsumo
import com.codemakers.aquaplus.domain.models.PersonaCliente
import com.codemakers.aquaplus.domain.models.UltimaLecturaHistorica
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
    val diasFactura: DiasFacturaDto?,
    val personaCliente: PersonaClienteDto,
)

@Serializable
data class DiasFacturaDto(
    val diasVencida: Int?,
)

@Serializable
data class EmpresaDto(
    val id: Int,
    val nit: String,
    val activo: Boolean?,
    val codigo: String,
    val nombre: String,
    val image: String,
    val direccion: DireccionDto?
)

@Serializable
data class ContadorDto(
    val id: Int,
    val deudas: List<DeudaDto>?,
    val serial: String,
    val idTipoContador: Int,
    val deudaAbonoSaldo: DeudaAbonoSaldoDto,
    val historicoConsumo: List<HistoricoConsumoDto>?,
    val nombreTipoContador: String,
    val ultimaLecturaHistorica: UltimaLecturaHistoricaDto?
)

@Serializable
data class DeudaDto(
    val id: Int,
    val valor: Double,
    val idFactura: Int,
    val descripcion: String,
    val fechaDeuda: String,
    val idPlazoPago: Int?,
    val idTipoDeuda: Int
)

@Serializable
data class DeudaAbonoSaldoDto(
    val deudaTotal: Double,
    val moraActual: Double,
    val abonosTotal: Double
)

@Serializable
data class HistoricoConsumoDto(
    val mes: String,
    val consumo: Int,
    val precio: Double
)

@Serializable
data class UltimaLecturaHistoricaDto(
    val id: Int,
    val lectura: Int,
    val fechaLectura: String,
    val idEccAsociado: Int,
    val usuarioCreacion: String,
    val personaCreacionNombre: String,
    val idPersonaClienteAsociada: Int
)

@Serializable
data class PersonaClienteDto(
    val id: Int,
    val codigo: String?,
    val estrato: Int,
    val direccion: DireccionDto,
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
        diasFactura = diasFactura?.toDomain(),
        personaCliente = personaCliente.toDomain(),
    )

fun DiasFacturaDto.toDomain(): DiasFactura = DiasFactura(
    diasVencida = diasVencida,
)

fun EmpresaDto.toDomain(): Empresa = Empresa(
    id = id,
    nit = nit,
    activo = activo,
    codigo = codigo,
    nombre = nombre,
    image = image,
    direccion = direccion?.toDomain(),
)

fun ContadorDto.toDomain(): Contador = Contador(
    id = id,
    deudas = deudas?.map { it.toDomain() },
    serial = serial,
    idTipoContador = idTipoContador,
    deudaAbonoSaldo = deudaAbonoSaldo.toDomain(),
    historicoConsumo = historicoConsumo?.map { it.toDomain() },
    nombreTipoContador = nombreTipoContador,
    ultimaLecturaHistorica = ultimaLecturaHistorica?.toDomain(),
)

fun DeudaDto.toDomain(): Deuda = Deuda(
    id = id,
    valor = valor,
    idFactura = idFactura,
    descripcion = descripcion,
    fechaDeuda = fechaDeuda,
    idPlazoPago = idPlazoPago,
    idTipoDeuda = idTipoDeuda,
)

fun DeudaAbonoSaldoDto.toDomain(): DeudaAbonoSaldo = DeudaAbonoSaldo(
    deudaTotal = deudaTotal,
    moraActual = moraActual,
    abonosTotal = abonosTotal,
)

fun HistoricoConsumoDto.toDomain(): HistoricoConsumo = HistoricoConsumo(
    mes = mes,
    consumo = consumo,
    precio = precio
)

fun UltimaLecturaHistoricaDto.toDomain(): UltimaLecturaHistorica = UltimaLecturaHistorica(
    id = id,
    lectura = lectura,
    fechaLectura = fechaLectura,
    idEccAsociado = idEccAsociado,
    usuarioCreacion = usuarioCreacion,
    personaCreacionNombre = personaCreacionNombre,
    idPersonaClienteAsociada = idPersonaClienteAsociada,
)

fun PersonaClienteDto.toDomain(): PersonaCliente = PersonaCliente(
    id = id,
    codigo = codigo,
    estrato = estrato,
    direccion = direccion.toDomain(),
    numeroCedula = numeroCedula,
    primerNombre = primerNombre,
    segundoNombre = segundoNombre,
    primerApellido = primerApellido,
    segundoApellido = segundoApellido,
)