package com.codemakers.aquaplus.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeRoute(
    val id: Int,
    val empresa: Empresa?,
    val contador: Contador?,
    val codFactura: String?,
    val codConvenio: String?,
    val ultimaFactura: UltimaFactura?,
    val personaCliente: PersonaCliente?
)

@Serializable
data class UltimaFactura(
    val fecha: String?,
    val codigo: String?,
    val precio: Double?,
    val lectura: Double?
)


@Serializable
data class Empresa(
    val id: Int?,
    val nit: String?,
    val activo: Boolean?,
    val codigo: String?,
    val nombre: String?,
    val direccion: Direccion?,
    val logoEmpresa: GenericEmpresa? = null,
    val puntosPago: List<GenericEmpresa>? = null,
    val codigoQr: GenericEmpresa? = null,
    val correoEmpresa: String? = null,
    val telefonoEmpresa: String? = null,
    val piePagina: String? = null,
    val avisoFactura: String? = null,
)

@Serializable
data class Contador(
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
    val tarifaContador: List<TarifaContador>?,
    val deudaAbonoSaldo: DeudaAbonoSaldo?,
    val promedioConsumo: Double?,
    val fechaInstalacion: String?,
    val historicoConsumo: List<HistoricoConsumo>?,
    val idEstadoContador: Int?,
    val lecturaProyectada: Double?,
    val nombreTipoContador: String?,
    val nombreEstadoContador: String?
)

@Serializable
data class TarifaContador(
    val aplica: Boolean?,
    val idTipoTarifa: Int?
)

@Serializable
data class DeudaCliente(
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
data class DeudaAbonoSaldo(
    val deudaTotal: Double?,
    val moraActual: Double?,
    val abonosTotal: Double?
)

@Serializable
data class HistoricoConsumo(
    val mes: String?,
    val consumo: Int?,
    val precio: Double?,
    val fechaLectura: String?
)


@Serializable
data class PersonaCliente(
    val id: Int,
    val codigo: String?,
    val direccion: Direccion?,
    val deudaCliente: List<DeudaCliente>?,
    val discapacidad: Boolean?,
    val numeroCedula: String?,
    val primerNombre: String?,
    val segundoNombre: String?,
    val primerApellido: String?,
    val segundoApellido: String?
)

@Serializable
data class Direccion(
    val ciudad: String?,
    val descripcion: String?,
    val departamento: String?,
    val corregimiento: String?
)

@Serializable
data class GenericEmpresa(
    val nombre: String?,
    val imagen: String?,
)
