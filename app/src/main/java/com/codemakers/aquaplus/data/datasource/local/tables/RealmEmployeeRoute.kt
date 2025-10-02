package com.codemakers.aquaplus.data.datasource.local.tables

import com.codemakers.aquaplus.domain.models.Contador
import com.codemakers.aquaplus.domain.models.Deuda
import com.codemakers.aquaplus.domain.models.DeudaAbonoSaldo
import com.codemakers.aquaplus.domain.models.DiasFactura
import com.codemakers.aquaplus.domain.models.Direccion
import com.codemakers.aquaplus.domain.models.EmployeeRoute
import com.codemakers.aquaplus.domain.models.Empresa
import com.codemakers.aquaplus.domain.models.GenericEmpresa
import com.codemakers.aquaplus.domain.models.HistoricoConsumo
import com.codemakers.aquaplus.domain.models.PersonaCliente
import com.codemakers.aquaplus.domain.models.UltimaLecturaHistorica
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class RealmEmployeeRoute : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var empresa: RealmEmpresa? = null
    var contador: RealmContador? = null
    var codFactura: String = ""
    var diasFactura: RealmDiasFactura? = null
    var personaCliente: RealmPersonaCliente? = null
}

open class RealmDiasFactura : EmbeddedRealmObject {
    var diasVencida: Int? = null
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
}

open class RealmContador : EmbeddedRealmObject {
    var id: Int = 0
    var deudas: RealmList<RealmDeuda> = realmListOf()
    var serial: String = ""
    var idTipoContador: Int = 0
    var deudaAbonoSaldo: RealmDeudaAbonoSaldo? = null
    var historicoConsumo: RealmList<RealmHistoricoConsumo> = realmListOf()
    var nombreTipoContador: String = ""
    var ultimaLecturaHistorica: RealmUltimaLecturaHistorica? = null
}

open class RealmDeuda : EmbeddedRealmObject {
    var id: Int = 0
    var valor: Double = 0.0
    var idFactura: Int = 0
    var descripcion: String = ""
    var fechaDeuda: String = ""
    var idPlazoPago: Int? = null
    var idTipoDeuda: Int = 0
}

open class RealmDeudaAbonoSaldo : EmbeddedRealmObject {
    var deudaTotal: Double = 0.0
    var moraActual: Double = 0.0
    var abonosTotal: Double = 0.0
}

open class RealmHistoricoConsumo : EmbeddedRealmObject {
    var mes: String = ""
    var consumo: Int = 0
    var precio: Double = 0.0
}

open class RealmUltimaLecturaHistorica : EmbeddedRealmObject {
    var id: Int = 0
    var lectura: Int = 0
    var fechaLectura: String = ""
    var idEccAsociado: Int = 0
    var usuarioCreacion: String = ""
    var personaCreacionNombre: String = ""
    var idPersonaClienteAsociada: Int = 0
}

open class RealmPersonaCliente : EmbeddedRealmObject {
    var id: Int = 0
    var codigo: String? = null
    var estrato: Int? = null
    var direccion: RealmDireccion? = null
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
        diasFactura = diasFactura?.toDomain(),
        personaCliente = personaCliente!!.toDomain(),
    )

fun RealmDiasFactura.toDomain(): DiasFactura = DiasFactura(
    diasVencida = diasVencida,
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
)

fun RealmContador.toDomain(): Contador = Contador(
    id = id,
    deudas = deudas.map { it.toDomain() },
    serial = serial,
    idTipoContador = idTipoContador,
    deudaAbonoSaldo = deudaAbonoSaldo!!.toDomain(),
    historicoConsumo = historicoConsumo.map { it.toDomain() },
    nombreTipoContador = nombreTipoContador,
    ultimaLecturaHistorica = ultimaLecturaHistorica?.toDomain(),
)

fun RealmDeuda.toDomain(): Deuda = Deuda(
    id = id,
    valor = valor,
    idFactura = idFactura,
    descripcion = descripcion,
    fechaDeuda = fechaDeuda,
    idPlazoPago = idPlazoPago,
    idTipoDeuda = idTipoDeuda,
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
)

fun RealmUltimaLecturaHistorica.toDomain(): UltimaLecturaHistorica = UltimaLecturaHistorica(
    id = id,
    lectura = lectura,
    fechaLectura = fechaLectura,
    idEccAsociado = idEccAsociado,
    usuarioCreacion = usuarioCreacion,
    personaCreacionNombre = personaCreacionNombre,
    idPersonaClienteAsociada = idPersonaClienteAsociada,
)

fun RealmPersonaCliente.toDomain(): PersonaCliente = PersonaCliente(
    id = id,
    codigo = codigo,
    estrato = estrato,
    direccion = direccion!!.toDomain(),
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

