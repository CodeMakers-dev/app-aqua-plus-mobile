package com.codemakers.aquaplus.data.datasource.local.tables

import com.codemakers.aquaplus.domain.models.Contador
import com.codemakers.aquaplus.domain.models.DeudaAbonoSaldo
import com.codemakers.aquaplus.domain.models.DeudaCliente
import com.codemakers.aquaplus.domain.models.Direccion
import com.codemakers.aquaplus.domain.models.EmployeeRoute
import com.codemakers.aquaplus.domain.models.Empresa
import com.codemakers.aquaplus.domain.models.GenericEmpresa
import com.codemakers.aquaplus.domain.models.HistoricoConsumo
import com.codemakers.aquaplus.domain.models.PersonaCliente
import com.codemakers.aquaplus.domain.models.TarifaContador
import com.codemakers.aquaplus.domain.models.UltimaFactura
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey

open class RealmEmployeeRoute : RealmObject {
    @PrimaryKey
    var id: Int = 0
    @Index
    var personId: Int = 0
    var empresa: RealmEmpresa? = null
    var contador: RealmContador? = null
    var codFactura: String = ""
    var ultimaFactura: RealmUltimaFactura? = null
    var personaCliente: RealmPersonaCliente? = null
}

open class RealmUltimaFactura : EmbeddedRealmObject {
    var fecha: String? = null
    var codigo: String? = null
    var precio: Double? = null
    var lectura: Int? = null
}


open class RealmEmpresa : EmbeddedRealmObject {
    var id: Int? = 0
    var nit: String? = ""
    var codigo: String? = ""
    var nombre: String? = ""
    var activo: Boolean? = null
    var direccion: RealmDireccion? = null
    var logoEmpresa: RealmGenericEmpresa? = null
    var puntosPago: RealmList<RealmGenericEmpresa> = realmListOf()
    var codigoQr: RealmGenericEmpresa? = null
    var correoEmpresa: String? = ""
    var telefonoEmpresa: String? = ""
    var piePagina: String? = ""
    var avisoFactura: String? = ""
}

open class RealmContador : EmbeddedRealmObject {
    var id: Int = 0
    var nuid: Long? = null
    var serial: String? = null
    var digitos: Int? = null
    var estrato: Int? = null
    var idTipoUso: Int? = null
    var matricula: String? = null
    var nombreTipoUso: String? = null
    var ultimaLectura: Double? = null
    var idTipoContador: Int? = null
    var tarifaContador: RealmList<RealmTarifaContador> = realmListOf()
    var deudaAbonoSaldo: RealmDeudaAbonoSaldo? = null
    var promedioConsumo: Double? = null
    var fechaInstalacion: String? = null
    var historicoConsumo: RealmList<RealmHistoricoConsumo> = realmListOf()
    var idEstadoContador: Int? = null
    var lecturaProyectada: Double? = null
    var nombreTipoContador: String = ""
    var nombreEstadoContador: String? = null
}

open class RealmTarifaContador : EmbeddedRealmObject {
    var aplica: Boolean? = null
    var idTipoTarifa: Int? = null
}

open class RealmDeudaCliente : EmbeddedRealmObject {
    var nuevoSaldo: Double? = null
    var valorCuota: Double? = null
    var idTipoDeuda: Int? = null
    var numeroCuotas: Int? = null
    var codigoTipoDeuda: String? = null
    var nombreTipoDeuda: String? = null
    var abonosRealizados: Int? = null
    var cuotasCanceladas: Int? = null
    var cuotasPendientes: Int? = null
}

open class RealmDeudaAbonoSaldo : EmbeddedRealmObject {
    var deudaTotal: Double = 0.0
    var moraActual: Double = 0.0
    var abonosTotal: Double = 0.0
}

open class RealmHistoricoConsumo : EmbeddedRealmObject {
    var mes: String? = null
    var consumo: Int? = null
    var precio: Double? = null
    var fechaLectura: String? = null
}


open class RealmPersonaCliente : EmbeddedRealmObject {
    var id: Int = 0
    var codigo: String? = null
    var direccion: RealmDireccion? = null
    var deudaCliente: RealmList<RealmDeudaCliente> = realmListOf()
    var discapacidad: Boolean? = null
    var numeroCedula: String = ""
    var primerNombre: String = ""
    var segundoNombre: String? = null
    var primerApellido: String = ""
    var segundoApellido: String? = null
}

open class RealmDireccion : EmbeddedRealmObject {
    var ciudad: String? = ""
    var descripcion: String? = ""
    var departamento: String? = ""
    var corregimiento: String? = ""
}

open class RealmGenericEmpresa : EmbeddedRealmObject {
    var nombre: String? = ""
    var imagen: String? = ""
}


fun RealmDireccion.toDomain(): Direccion = Direccion(
    ciudad = ciudad,
    descripcion = descripcion,
    departamento = departamento,
    corregimiento = corregimiento
)

fun RealmEmployeeRoute.toDomain(): EmployeeRoute =
    EmployeeRoute(
        id = id,
        empresa = empresa!!.toDomain(),
        contador = contador!!.toDomain(),
        codFactura = codFactura,
        ultimaFactura = ultimaFactura?.toDomain(),
        personaCliente = personaCliente!!.toDomain(),
    )

fun RealmUltimaFactura.toDomain(): UltimaFactura = UltimaFactura(
    fecha = fecha,
    codigo = codigo,
    precio = precio,
    lectura = lectura
)


fun RealmEmpresa.toDomain(): Empresa = Empresa(
    id = id,
    nit = nit,
    codigo = codigo,
    nombre = nombre,
    direccion = direccion?.toDomain(),
    activo = activo,
    logoEmpresa = logoEmpresa?.toDomain(),
    puntosPago = puntosPago.map { it.toDomain() },
    codigoQr = codigoQr?.toDomain(),
    correoEmpresa = correoEmpresa,
    telefonoEmpresa = telefonoEmpresa,
    piePagina = piePagina,
    avisoFactura = avisoFactura
)

fun RealmContador.toDomain(): Contador = Contador(
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
    tarifaContador = tarifaContador.map { it.toDomain() },
    deudaAbonoSaldo = deudaAbonoSaldo!!.toDomain(),
    promedioConsumo = promedioConsumo,
    fechaInstalacion = fechaInstalacion,
    historicoConsumo = historicoConsumo.map { it.toDomain() },
    idEstadoContador = idEstadoContador,
    lecturaProyectada = lecturaProyectada,
    nombreTipoContador = nombreTipoContador,
    nombreEstadoContador = nombreEstadoContador
)

fun RealmTarifaContador.toDomain(): TarifaContador = TarifaContador(
    aplica = aplica,
    idTipoTarifa = idTipoTarifa
)

fun RealmDeudaCliente.toDomain(): DeudaCliente = DeudaCliente(
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

fun RealmDeudaAbonoSaldo.toDomain(): DeudaAbonoSaldo = DeudaAbonoSaldo(
    deudaTotal = deudaTotal,
    moraActual = moraActual,
    abonosTotal = abonosTotal,
)

fun RealmHistoricoConsumo.toDomain(): HistoricoConsumo = HistoricoConsumo(
    mes = mes,
    consumo = consumo,
    precio = precio,
    fechaLectura = fechaLectura
)


fun RealmPersonaCliente.toDomain(): PersonaCliente = PersonaCliente(
    id = id,
    codigo = codigo,
    direccion = direccion!!.toDomain(),
    deudaCliente = deudaCliente.map { it.toDomain() },
    discapacidad = discapacidad,
    numeroCedula = numeroCedula,
    primerNombre = primerNombre,
    segundoNombre = segundoNombre,
    primerApellido = primerApellido,
    segundoApellido = segundoApellido,
)

fun RealmGenericEmpresa.toDomain(): GenericEmpresa = GenericEmpresa(
    nombre = nombre,
    imagen = imagen,
)

