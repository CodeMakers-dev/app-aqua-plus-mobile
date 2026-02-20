package com.codemakers.aquaplus.ui.models

import com.codemakers.aquaplus.domain.models.DeudaAbonoSaldo
import com.codemakers.aquaplus.domain.models.DeudaCliente
import com.codemakers.aquaplus.domain.models.EmployeeRoute
import com.codemakers.aquaplus.domain.models.EmployeeRouteConfig
import com.codemakers.aquaplus.domain.models.ReadingFormData
import com.codemakers.aquaplus.ui.extensions.containsAnyOf
import com.codemakers.aquaplus.ui.extensions.roundTo2Decimals
import com.codemakers.aquaplus.ui.extensions.toCapitalCase
import com.codemakers.aquaplus.ui.extensions.toLocalDate
import java.time.LocalDate

const val CONBAS = "BAS"
const val CONSUN = "SUN"
const val CONCOM = "COM"

data class Client(
    val name: String,
    val idLabel: String,
    val id: String,
    val address: String,
    val city: String,
    val code: String
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
    val stratum: Int,
    val nameTypeUse: String,
    val state: String,
    val registration: String,
    val average: Double
)

data class ReadingInfo(
    val prevReading: Double,
    val prevDate: LocalDate?,
    val currentReading: Double,
    val currentDate: LocalDate,
    val consumptionM3: Double,
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
    val consumption: Double?,
    val valorMcValue: Double? = null,
) {

    val tarifa: Double?
        get() {
            val conceptValue = stratumValue?.value ?: value
            return if (indCalcularMc == true && valorMcValue != null) {
                (valorMcValue + (conceptValue ?: 0.0)).roundTo2Decimals()
            } else {
                conceptValue?.roundTo2Decimals()
            }
        }

    val consumptionTotal: Double?
        get() = if (indCalcularMc == true) consumption else null

    val total: Double
        get() {
            val total = tarifa ?: 0.0
            val factor = if (indCalcularMc == true) (consumption ?: 0.0) else 1.0
            return total * factor
        }
}

data class StratumValueDetail(
    val value: Double,
    val stratum: Int,
)

data class HistoryEntry(
    val mes: String?,
    val precio: Double?,
    val consumo: Int?
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
    val companyAddress: String,
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
    val deudaAbonoSaldo: DeudaAbonoSaldo?,
    val deudaCliente: List<DeudaCliente>?,
    val codConvenio: String?,
) {

    val totals: Totals
        get() {
            val subtotal = fees.sumOf { it.conceptos?.sumOf { it.total } ?: 0.0 }
            val previousBalance = deudaAbonoSaldo?.moraActual ?: 0.0
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
        companyName = route.empresa?.nombre.orEmpty(),
        companyNit = route.empresa?.nit.orEmpty(),
        companyAddress = listOfNotNull(
            config.config?.empresa?.direccion?.corregimiento,
            config.config?.empresa?.direccion?.ciudad,
            config.config?.empresa?.direccion?.departamento
        ).filter { it.isNotBlank() }.joinToString(", "),
        companyQrCode = config.config?.empresa?.codigoQr?.imagen,
        methodsPayment = config.config?.empresa?.puntosPago?.map { it.imagen },
        codInvoice = route.codFactura.orEmpty(),
        codConvenio = route.codConvenio.orEmpty(),
        companyFooter = config.config?.empresa?.piePagina,
        companyFooterNote = config.config?.empresa?.avisoFactura,
        client = Client(
            name = "${route.personaCliente?.primerNombre} ${route.personaCliente?.primerApellido}".toCapitalCase(),
            idLabel = route.personaCliente?.codigo.orEmpty(),
            id = route.personaCliente?.numeroCedula.orEmpty(),
            address = route.personaCliente?.direccion?.descripcion ?: "---",
            city = listOfNotNull(
                route.personaCliente?.direccion?.corregimiento?.takeIf { it.isNotBlank() },
                route.personaCliente?.direccion?.ciudad,
                route.personaCliente?.direccion?.departamento
            ).joinToString(", "),
            code = route.personaCliente?.codigo.orEmpty()
        ),
        meta = InvoiceMeta(
            issueDate = LocalDate.now(),
            dueDate = config.config?.parametrosEmpresa?.diasVencida?.toIntOrNull()
                ?.let { LocalDate.now().plusDays(it.toLong()) }
                ?: LocalDate.now(),
            payType = "Pendiente",
            state = "Pendiente",
        ),
        meter = MeterInfo(
            number = data.serial ?: route.contador?.serial.orEmpty(),
            installDate = route.contador?.fechaInstalacion?.toLocalDate(),
            type = route.contador?.nombreTipoContador.orEmpty(),
            stratum = route.contador?.estrato ?: 0,
            nameTypeUse = route.contador?.nombreTipoUso.orEmpty(),
            state = config.config?.estadosMedidor?.find { it.id == data.meterStateId }?.descripcion.orEmpty(),
            registration = route.contador?.matricula.orEmpty(),
            average = route.contador?.promedioConsumo ?: 0.0
        ),
        reading = ReadingInfo(
            prevReading = route.ultimaFactura?.lectura ?: 0.0,
            prevDate = route.ultimaFactura?.fecha?.toLocalDate(),
            currentReading = data.meterReading.toDouble(),
            currentDate = data.date,
            consumptionM3 = ((data.meterReading.toDouble()) - (route.contador?.ultimaLectura
                ?: 0.0)).roundTo2Decimals(),
            lastPaymentValue = route.ultimaFactura?.precio,
            lastPaymentDate = route.ultimaFactura?.fecha?.toLocalDate(),
        ),
        fees = config.config?.let { cfg ->
            val consumption = (data.meterReading.toDouble()) - (route.contador?.ultimaLectura ?: 0.0)
            val consumptionCodes = setOf(CONBAS, CONSUN, CONCOM)

            cfg.tarifasEmpresa?.mapNotNull { tarifa ->
                val consuBasico = tarifa.rangoConsumo?.consuBasico?.toDoubleOrNull()
                val consuSuntuario = tarifa.rangoConsumo?.consuSuntuario?.toDoubleOrNull()
                val consuComplementario = tarifa.rangoConsumo?.consuComplementario?.toDoubleOrNull()

                val targetConceptCode = when {
                    consuBasico != null && consumption <= consuBasico -> CONBAS
                    consuComplementario != null && consumption <= consuComplementario -> CONCOM
                    consuSuntuario != null && consumption > consuSuntuario -> CONSUN
                    else -> CONBAS
                }

                val idTipoTarifa = tarifa.tipoTarifa?.id ?: -1
                if (route.contador?.tarifaContador?.map { it.idTipoTarifa }?.contains(idTipoTarifa) == true) {
                    return@mapNotNull null
                }

                val activeConceptCode = tarifa.conceptos
                    ?.filter { it.tipoConcepto?.codigo.containsAnyOf(consumptionCodes) }
                    ?.let { codes ->
                        codes.find { it.tipoConcepto?.codigo?.contains(targetConceptCode) == true }?.tipoConcepto?.codigo
                            ?: codes.find { it.tipoConcepto?.codigo?.contains(CONBAS) == true }?.tipoConcepto?.codigo
                    }

                FeeSection(
                    id = tarifa.id,
                    title = tarifa.tipoTarifa?.nombre.orEmpty(),
                    code = tarifa.tipoTarifa?.codigo.orEmpty(),
                    conceptos = tarifa.conceptos?.map { concept ->
                        val isActive = concept.tipoConcepto?.codigo?.contains(activeConceptCode.orEmpty()) == true || !concept.tipoConcepto?.codigo.containsAnyOf(consumptionCodes)

                        val valorMc = if (isActive && concept.indCalcularMc == true) {
                            tarifa.valorMc?.find { mc -> mc.tipoUso?.id == route.contador?.idTipoUso && mc.estrato == route.contador?.estrato }
                        } else null

                        ConceptDetail(
                            id = concept.id,
                            title = concept.tipoConcepto?.descripcion.orEmpty(),
                            code = concept.tipoConcepto?.codigo.orEmpty(),
                            indCalcularMc = if (isActive) concept.indCalcularMc else false,
                            stratumValue = concept.valoresEstrato?.find { valueStratum -> valueStratum.estrato == route.contador?.estrato }
                                ?.let { stratum ->
                                    StratumValueDetail(
                                        value = stratum.valor ?: 0.0,
                                        stratum = stratum.estrato ?: 0,
                                    )
                                },
                            value = if (isActive) concept.valor else 0.0,
                            consumption = if (isActive) consumption else null,
                            valorMcValue = valorMc?.valor,
                        )
                    }
                )
            }.orEmpty()
        }.orEmpty(),
        history = route.contador?.historicoConsumo?.mapNotNull {
            if (it.mes != null && it.precio != null && it.consumo != null) {
                HistoryEntry(
                    mes = it.mes,
                    precio = it.precio,
                    consumo = it.consumo,
                )
            } else null
        }.orEmpty(),
        verificationCode = "Pendiente",
        observations = data.observations,
        deudaAbonoSaldo = route.contador?.deudaAbonoSaldo,
        deudaCliente = route.personaCliente?.deudaCliente,
    )
}
