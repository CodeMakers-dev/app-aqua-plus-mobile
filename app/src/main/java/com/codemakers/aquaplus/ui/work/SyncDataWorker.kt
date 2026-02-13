package com.codemakers.aquaplus.ui.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.codemakers.aquaplus.domain.models.InvoiceRequest
import com.codemakers.aquaplus.domain.models.ReadRequest
import com.codemakers.aquaplus.domain.usecases.GetAllReadingFormDataForSyncUseCase
import com.codemakers.aquaplus.domain.usecases.GetEmployeeRouteAndConfigByIdUseCase
import com.codemakers.aquaplus.domain.usecases.SendInvoiceUseCase
import com.codemakers.aquaplus.domain.usecases.UpdateReadingFormDataIsSyncedUseCase
import com.codemakers.aquaplus.ui.models.Invoice

class SyncDataWorker(
    context: Context,
    params: WorkerParameters,
    private val sendInvoiceUseCase: SendInvoiceUseCase,
    private val getAllReadingFormDataForSyncUseCase: GetAllReadingFormDataForSyncUseCase,
    private val getEmployeeRouteAndConfigByIdUseCase: GetEmployeeRouteAndConfigByIdUseCase,
    private val updateReadingFormDataIsSyncedUseCase: UpdateReadingFormDataIsSyncedUseCase,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val syncResult = getAllReadingFormDataForSyncUseCase()
        val data = syncResult.getOrNull
        if (data.isNullOrEmpty()) return Result.failure()

        val groupedByPerson = data.groupBy { it.personId }

        var hasAnySuccess = false

        groupedByPerson.forEach { (personId, readingFormDataList) ->
            val request = mutableListOf<InvoiceRequest>()
            val employeeRouteIds = mutableListOf<Int>()

            readingFormDataList.forEach { readingFormData ->
                val employeeRouteId = readingFormData.employeeRouteId
                val routeResult = getEmployeeRouteAndConfigByIdUseCase(
                    employeeRouteId = employeeRouteId,
                    personId = personId,
                )
                val employeeRouteAndConfig = routeResult.getOrNull
                val employeeRoute = employeeRouteAndConfig?.first
                val employeeRouteConfig = employeeRouteAndConfig?.second

                if (employeeRoute == null || employeeRouteConfig == null) return@forEach

                val invoice = Invoice(
                    route = employeeRoute,
                    config = employeeRouteConfig,
                    data = readingFormData
                )

                employeeRouteIds.add(employeeRouteId)

                request.add(
                    InvoiceRequest(
                        code = employeeRoute.codFactura,
                        idContador = employeeRoute.id,
                        precio = invoice.totals.totalToPay,
                        fechaEmision = readingFormData.date.toString(),
                        usuarioCreacion = "",
                        codEstado = "PEN",
                        lectura = ReadRequest(
                            meterReading = readingFormData.meterReading,
                            description = readingFormData.observations,
                            abnormalConsumption = readingFormData.abnormalConsumption == true
                        ),
                    )
                )
            }

            if (request.isEmpty()) return@forEach

            val requestResult = sendInvoiceUseCase(personId = personId, request = request)
            Log.d("SyncDataWorker", "requestResult: $requestResult")
            if (requestResult.isSuccess) {
                updateReadingFormDataIsSyncedUseCase(employeeRouteIds)
                hasAnySuccess = true
            }
        }

        return if (hasAnySuccess) Result.success() else Result.failure()
    }
}