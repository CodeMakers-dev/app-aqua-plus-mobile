package com.codemakers.aquaplus.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeRoute(
    val id: Int,
    val empresa: Empresa,
    val contador: Contador,
    val codFactura: String,
    val diasFactura: DiasFactura?,
    val ultimaFactura: UltimaFactura?,
    val personaCliente: PersonaCliente
)

@Serializable
data class UltimaFactura(
    val fecha: String?,
    val precio: Double?,
)

@Serializable
data class DiasFactura(
    val diasVencida: Int?,
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
    val deudas: List<Deuda>?,
    val serial: String,
    val idTipoContador: Int,
    val deudaAbonoSaldo: DeudaAbonoSaldo,
    val fechaInstalacion: String?,
    val historicoConsumo: List<HistoricoConsumo>?,
    val nombreTipoContador: String,
    val ultimaLecturaHistorica: UltimaLecturaHistorica?
)

@Serializable
data class Deuda(
    val id: Int,
    val valor: Double,
    val idFactura: Int,
    val descripcion: String,
    val fechaDeuda: String,
    val idPlazoPago: Int?,
    val idTipoDeuda: Int
)

@Serializable
data class DeudaAbonoSaldo(
    val deudaTotal: Double,
    val moraActual: Double,
    val abonosTotal: Double
)

@Serializable
data class HistoricoConsumo(
    val mes: String,
    val consumo: Int,
    val precio: Double,
)

@Serializable
data class UltimaLecturaHistorica(
    val id: Int,
    val lectura: Int,
    val fechaLectura: String,
    val idEccAsociado: Int,
    val usuarioCreacion: String,
    val personaCreacionNombre: String,
    val idPersonaClienteAsociada: Int
)

@Serializable
data class PersonaCliente(
    val id: Int,
    val codigo: String?,
    val estrato: Int?,
    val direccion: Direccion,
    val numeroCedula: String,
    val primerNombre: String,
    val segundoNombre: String?,
    val primerApellido: String,
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
