package com.codemakers.aquaplus.ui.models

import com.codemakers.aquaplus.domain.models.DeudaAbonoSaldo
import com.codemakers.aquaplus.domain.models.EmployeeRoute
import com.codemakers.aquaplus.domain.models.EmployeeRouteConfig
import com.codemakers.aquaplus.domain.models.ReadingFormData
import com.codemakers.aquaplus.ui.extensions.toCapitalCase
import java.time.LocalDate


data class Client(
    val name: String,
    val idLabel: String,
    val id: String,
    val address: String,
    val city: String,
)

data class InvoiceMeta(
    val issueDate: LocalDate,
    val dueDate: LocalDate,
    val payType: String,
    val state: String,
)

data class MeterInfo(
    val number: String,
    val installDate: LocalDate?,
    val type: String,
)

data class ReadingInfo(
    val prevReading: Int,
    val prevDate: LocalDate?,
    val currentReading: Int,
    val currentDate: LocalDate,
    val consumptionM3: Int,
    val lastPaymentValue: Double?,
    val lastPaymentDate: LocalDate?,
)

data class FeeSection(
    val id: Int?,
    val title: String,
    val code: String,
    val conceptos: List<ConceptDetail>?,
)

data class ConceptDetail(
    val id: Int?,
    val title: String,
    val code: String,
    val indCalcularMc: Boolean?,
    val stratumValue: StratumValueDetail?,
    val value: Double?,
    val consumption: Int?,
) {

    val tarifa: Double?
        get() = value ?: stratumValue?.value

    val consumptionTotal: Int?
        get() = if (indCalcularMc == true) consumption else null

    val total: Double
        get() {
            val total = tarifa ?: 0.0
            val factor = if (indCalcularMc == true) (consumption?.toDouble() ?: 0.0) else 1.0
            return total * factor
        }
}

data class StratumValueDetail(
    val value: Double,
    val stratum: Int,
)

data class HistoryEntry(
    val mes: String,
    val precio: Double,
    val consumo: Int
)

data class Totals(
    val subtotal: Double,
    val previousBalance: Double,
    val totalToPay: Double,
)

data class Invoice(
    val companyImage: String?,
    val companyName: String,
    val companyNit: String,
    val companyCode: String,
    val companyQrCode: String?,
    val companyFooter: String?,
    val companyFooterNote: String?,
    val methodsPayment: List<String?>? = emptyList(),
    val codInvoice: String,
    val client: Client,
    val meta: InvoiceMeta,
    val meter: MeterInfo,
    val reading: ReadingInfo,
    val fees: List<FeeSection>,
    val history: List<HistoryEntry>,
    val verificationCode: String,
    val observations: String,
    val deudaAbonoSaldo: DeudaAbonoSaldo,
) {

    val totals: Totals
        get() {
            val subtotal = fees.sumOf { it.conceptos?.sumOf { it.total } ?: 0.0 }
            val previousBalance = deudaAbonoSaldo.moraActual
            return Totals(
                subtotal = subtotal,
                previousBalance = previousBalance,
                totalToPay = subtotal + previousBalance,
            )
        }

    constructor(
        route: EmployeeRoute,
        config: EmployeeRouteConfig,
        data: ReadingFormData,
    ) : this(
        companyImage = config.config?.empresa?.logoEmpresa?.imagen,
        companyName = route.empresa.nombre ?: "",
        companyNit = route.empresa.nit ?: "",
        companyCode = route.empresa.codigo ?: "",
        companyQrCode = config.config?.empresa?.codigoQr?.imagen,
        methodsPayment = config.config?.empresa?.puntosPago?.map { it.imagen },
        codInvoice = route.codFactura,
        companyFooter = config.config?.empresa?.piePagina,
        companyFooterNote = config.config?.empresa?.avisoFactura,
        client = Client(
            name = "${route.personaCliente.primerNombre} ${route.personaCliente.primerApellido}".toCapitalCase(),
            idLabel = route.personaCliente.codigo.orEmpty(),
            id = route.personaCliente.numeroCedula,
            address = route.personaCliente.direccion.descripcion ?: "---",
            city = listOfNotNull(
                route.personaCliente.direccion.corregimiento?.takeIf { it.isNotBlank() },
                route.personaCliente.direccion.ciudad,
                route.personaCliente.direccion.departamento
            ).joinToString(", "),
        ),
        meta = InvoiceMeta(
            issueDate = LocalDate.now(),
            dueDate = route.diasFactura?.diasVencida?.let { LocalDate.now().plusDays(it.toLong()) }
                ?: LocalDate.now(),
            payType = "Pendiente",
            state = "Pendiente",
        ),
        meter = MeterInfo(
            number = route.contador.serial,
            installDate = route.contador.fechaInstalacion?.toLocalDate(),
            type = route.contador.nombreTipoContador,
        ),
        reading = ReadingInfo(
            prevReading = route.contador.ultimaLecturaHistorica?.lectura ?: 0,
            prevDate = route.contador.ultimaLecturaHistorica?.fechaLectura?.toLocalDate(),
            currentReading = data.meterReading.toInt(),
            currentDate = data.date,
            consumptionM3 = data.meterReading.toInt() - (route.contador.ultimaLecturaHistorica?.lectura
                ?: 0),
            lastPaymentValue = route.ultimaFactura?.precio,
            lastPaymentDate = route.ultimaFactura?.fecha?.toLocalDate(),
        ),
        fees = config.config?.tarifasEmpresa?.map {
            if (it.tipoTarifa?.codigo == "OTR") {
                FeeSection(
                    id = it.id,
                    title = it.tipoTarifa.descripcion.orEmpty(),
                    code = it.tipoTarifa.codigo,
                    conceptos = route.contador.deudas?.map { deuda ->
                        ConceptDetail(
                            id = deuda.id,
                            title = deuda.descripcion,
                            code = "",
                            indCalcularMc = false,
                            stratumValue = null,
                            value = deuda.valor,
                            consumption = null,
                        )
                    }
                )
            } else {
                FeeSection(
                    id = it.id,
                    title = it.tipoTarifa?.descripcion.orEmpty(),
                    code = it.tipoTarifa?.codigo.orEmpty(),
                    conceptos = it.conceptos?.map { concept ->
                        ConceptDetail(
                            id = concept.id,
                            title = concept.tipoConcepto?.descripcion.orEmpty(),
                            code = concept.tipoConcepto?.codigo.orEmpty(),
                            indCalcularMc = concept.indCalcularMc,
                            stratumValue = concept.valoresEstrato?.find { valueStratum -> valueStratum.estrato == route.personaCliente.estrato }
                                ?.let { stratum ->
                                    StratumValueDetail(
                                        value = stratum.valor ?: 0.0,
                                        stratum = stratum.estrato ?: 0,
                                    )
                                },
                            value = concept.valor,
                            consumption = data.meterReading.toInt() - (route.contador.ultimaLecturaHistorica?.lectura
                                ?: 0),
                        )
                    }
                )
            }
        }.orEmpty(),
        history = route.contador.historicoConsumo?.map {
            HistoryEntry(
                mes = it.mes,
                precio = it.precio,
                consumo = it.consumo,
            )
        }.orEmpty(),
        verificationCode = "Pendiente",
        observations = data.observations,
        deudaAbonoSaldo = route.contador.deudaAbonoSaldo,
    )
}

fun String.toLocalDate(): LocalDate? = try {
    val localDate = LocalDate.parse(this)
    println("Parsed LocalDate 1: $localDate")
    localDate
} catch (e: Exception) {
    println("Error parsing date string 1: ${e.message}")
    null
}
