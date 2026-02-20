package com.codemakers.aquaplus.data.datasource.local.tables

import com.codemakers.aquaplus.domain.models.Concepto
import com.codemakers.aquaplus.domain.models.Config
import com.codemakers.aquaplus.domain.models.EmployeeRouteConfig
import com.codemakers.aquaplus.domain.models.EstadoMedidor
import com.codemakers.aquaplus.domain.models.ParametrosEmpresa
import com.codemakers.aquaplus.domain.models.RangoConsumo
import com.codemakers.aquaplus.domain.models.TarifaEmpresa
import com.codemakers.aquaplus.domain.models.TipoConcepto
import com.codemakers.aquaplus.domain.models.TipoTarifa
import com.codemakers.aquaplus.domain.models.TipoUso
import com.codemakers.aquaplus.domain.models.TipoUsoWrapper
import com.codemakers.aquaplus.domain.models.ValorEstrato
import com.codemakers.aquaplus.domain.models.ValorMc
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey

open class RealmEmployeeRouteConfig : RealmObject {
    @PrimaryKey
    var id: Int = 0
    @Index
    var personId: Int = 0
    var config: RealmConfig? = null
}

open class RealmConfig : EmbeddedRealmObject {
    var empresa: RealmEmpresa? = null
    var tipoUso: RealmTipoUsoWrapper? = null
    var estadosMedidor: RealmList<RealmEstadoMedidor>? = null
    var tarifasEmpresa: RealmList<RealmTarifaEmpresa>? = null
    var parametrosEmpresa: RealmParametrosEmpresa? = null
}

open class RealmTarifaEmpresa : EmbeddedRealmObject {
    var id: Int? = null
    var empresa: Int? = null
    var valorMc: RealmList<RealmValorMc>? = null
    var conceptos: RealmList<RealmConcepto>? = null
    var tipoTarifa: RealmTipoTarifa? = null
    var rangoConsumo: RealmRangoConsumo? = null
}

open class RealmRangoConsumo : EmbeddedRealmObject {
    var consuBasico: String? = null
    var consuSuntuario: String? = null
    var consuComplementario: String? = null
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

open class RealmTipoUsoWrapper : EmbeddedRealmObject {
    var data: RealmList<RealmTipoUso>? = null
}

open class RealmTipoUso : EmbeddedRealmObject {
    var id: Int? = null
    var activo: Boolean? = null
    var codigo: String? = null
    var nombre: String? = null
    var descripcion: String? = null
}

open class RealmEstadoMedidor : EmbeddedRealmObject {
    var id: Int? = null
    var codigo: String? = null
    var descripcion: String? = null
}

open class RealmValorMc : EmbeddedRealmObject {
    var id: Int? = null
    var rango: String? = null
    var valor: Double? = null
    var codigo: String? = null
    var nombre: String? = null
    var estrato: Int? = null
    var tipoUso: RealmTipoUso? = null
}

open class RealmParametrosEmpresa : EmbeddedRealmObject {
    var consuBasico: String? = null
    var diasVencida: String? = null
    var periodosInm: String? = null
    var periodosVig: String? = null
    var periodosFact: String? = null
    var consuSuntuario: String? = null
    var consuComplementario: String? = null
}

fun RealmEmployeeRouteConfig.toDomain(): EmployeeRouteConfig = EmployeeRouteConfig(
    config = config?.toDomain()
)

fun RealmConfig.toDomain(): Config = Config(
    empresa = empresa?.toDomain(),
    tipoUso = tipoUso?.toDomain(),
    estadosMedidor = estadosMedidor?.map { it.toDomain() },
    tarifasEmpresa = tarifasEmpresa?.map { it.toDomain() },
    parametrosEmpresa = parametrosEmpresa?.toDomain()
)

fun RealmTarifaEmpresa.toDomain(): TarifaEmpresa = TarifaEmpresa(
    id = id,
    empresa = empresa,
    valorMc = valorMc?.map { it.toDomain() },
    conceptos = conceptos?.map { it.toDomain() },
    tipoTarifa = tipoTarifa?.toDomain(),
    rangoConsumo = rangoConsumo?.toDomain()
)

fun RealmRangoConsumo.toDomain(): RangoConsumo = RangoConsumo(
    consuBasico = consuBasico,
    consuSuntuario = consuSuntuario,
    consuComplementario = consuComplementario
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

fun RealmTipoUsoWrapper.toDomain(): TipoUsoWrapper = TipoUsoWrapper(
    data = data?.map { it.toDomain() }
)

fun RealmTipoUso.toDomain(): TipoUso = TipoUso(
    id = id,
    activo = activo,
    codigo = codigo,
    nombre = nombre,
    descripcion = descripcion
)

fun RealmEstadoMedidor.toDomain(): EstadoMedidor = EstadoMedidor(
    id = id,
    codigo = codigo,
    descripcion = descripcion
)

fun RealmValorMc.toDomain(): ValorMc = ValorMc(
    id = id,
    rango = rango,
    valor = valor,
    codigo = codigo,
    nombre = nombre,
    estrato = estrato,
    tipoUso = tipoUso?.toDomain()
)

fun RealmParametrosEmpresa.toDomain(): ParametrosEmpresa = ParametrosEmpresa(
    consuBasico = consuBasico,
    diasVencida = diasVencida,
    periodosInm = periodosInm,
    periodosVig = periodosVig,
    periodosFact = periodosFact,
    consuSuntuario = consuSuntuario,
    consuComplementario = consuComplementario
)

