package com.codemakers.aquaplus.data.datasource.local.tables

import com.codemakers.aquaplus.domain.models.Catalogos
import com.codemakers.aquaplus.domain.models.Concepto
import com.codemakers.aquaplus.domain.models.Config
import com.codemakers.aquaplus.domain.models.EmployeeRouteConfig
import com.codemakers.aquaplus.domain.models.TarifaEmpresa
import com.codemakers.aquaplus.domain.models.TipoConcepto
import com.codemakers.aquaplus.domain.models.TipoTarifa
import com.codemakers.aquaplus.domain.models.ValorEstrato
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class RealmEmployeeRouteConfig : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var config: RealmConfig? = null
    var catalogos: RealmCatalogos? = null
}

open class RealmConfig : EmbeddedRealmObject {
    var empresa: RealmEmpresa? = null
    var tarifasEmpresa: RealmList<RealmTarifaEmpresa>? = null
}

open class RealmTarifaEmpresa : EmbeddedRealmObject {
    var id: Int? = null
    var empresa: Int? = null
    var conceptos: RealmList<RealmConcepto>? = null
    var tipoTarifa: RealmTipoTarifa? = null
}

open class RealmConcepto : EmbeddedRealmObject {
    var id: Int? = null
    var valor: Double? = null
    var tipoConcepto: RealmTipoConcepto? = null
    var indCalcularMc: Boolean? = null
    var valoresEstrato: RealmList<RealmValorEstrato>? = null
}

open class RealmTipoConcepto : EmbeddedRealmObject {
    var id: Int? = null
    var codigo: String? = null
    var descripcion: String? = null
}

open class RealmValorEstrato : EmbeddedRealmObject {
    var id: Int? = null
    var valor: Double? = null
    var estrato: Int? = null
}

open class RealmTipoTarifa : EmbeddedRealmObject {
    var id: Int? = null
    var codigo: String? = null
    var nombre: String? = null
    var descripcion: String? = null
}

open class RealmCatalogos : EmbeddedRealmObject {
    var tiposTarifa: RealmList<RealmTipoTarifa>? = null
    var tiposConcepto: RealmList<RealmTipoConcepto>? = null
}

fun RealmEmployeeRouteConfig.toDomain(): EmployeeRouteConfig = EmployeeRouteConfig(
    config = config?.toDomain(),
    catalogos = catalogos?.toDomain()
)

fun RealmConfig.toDomain(): Config = Config(
    empresa = empresa?.toDomain(),
    tarifasEmpresa = tarifasEmpresa?.map { it.toDomain() }
)

fun RealmTarifaEmpresa.toDomain(): TarifaEmpresa = TarifaEmpresa(
    id = id,
    empresa = empresa,
    conceptos = conceptos?.map { it.toDomain() },
    tipoTarifa = tipoTarifa?.toDomain()
)

fun RealmConcepto.toDomain(): Concepto = Concepto(
    id = id,
    valor = valor,
    tipoConcepto = tipoConcepto?.toDomain(),
    indCalcularMc = indCalcularMc,
    valoresEstrato = valoresEstrato?.map { it.toDomain() }
)

fun RealmTipoConcepto.toDomain(): TipoConcepto = TipoConcepto(
    id = id,
    codigo = codigo,
    descripcion = descripcion,
)

fun RealmValorEstrato.toDomain(): ValorEstrato = ValorEstrato(
    id = id,
    valor = valor,
    estrato = estrato
)

fun RealmTipoTarifa.toDomain(): TipoTarifa = TipoTarifa(
    id = id,
    codigo = codigo,
    nombre = nombre,
    descripcion = descripcion
)

fun RealmCatalogos.toDomain(): Catalogos = Catalogos(
    tiposTarifa = tiposTarifa?.map { it.toDomain() },
    tiposConcepto = tiposConcepto?.map { it.toDomain() }
)

