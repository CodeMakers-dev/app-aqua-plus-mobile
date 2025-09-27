package com.codemakers.aquaplus.ui.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.codemakers.aquaplus.domain.models.InvoiceRequest
import com.codemakers.aquaplus.domain.models.ReadRequest
import com.codemakers.aquaplus.domain.repository.InvoiceRepository
import com.codemakers.aquaplus.domain.repository.UserRepository
import com.codemakers.aquaplus.domain.usecases.GetAllReadingFormDataForSyncUseCase
import com.codemakers.aquaplus.domain.usecases.GetEmployeeRouteAndConfigByIdUseCase
import com.codemakers.aquaplus.domain.usecases.UpdateReadingFormDataIsSyncedUseCase
import com.codemakers.aquaplus.ui.models.Invoice

class SyncDataWorker(
    context: Context,
    params: WorkerParameters,
    private val userRepository: UserRepository,
    private val invoiceRepository: InvoiceRepository,
    private val getAllReadingFormDataForSyncUseCase: GetAllReadingFormDataForSyncUseCase,
    private val getEmployeeRouteAndConfigByIdUseCase: GetEmployeeRouteAndConfigByIdUseCase,
    private val updateReadingFormDataIsSyncedUseCase: UpdateReadingFormDataIsSyncedUseCase,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val userName = userRepository.getProfile()?.username

        val result = getAllReadingFormDataForSyncUseCase()
        val data = result.getOrNull
        if (data.isNullOrEmpty()) return Result.failure()

        val request = mutableListOf<InvoiceRequest>()
        data.forEach { readingFormData ->
            val employeeRouteId = readingFormData.employeeRouteId
            val result = getEmployeeRouteAndConfigByIdUseCase(employeeRouteId = employeeRouteId)
            val employeeRouteAndConfig = result.getOrNull
            val employeeRoute = employeeRouteAndConfig?.first
            val employeeRouteConfig = employeeRouteAndConfig?.second

            if (employeeRoute == null || employeeRouteConfig == null) return@forEach

            val invoice = Invoice(
                route = employeeRoute,
                config = employeeRouteConfig,
                data = readingFormData
            )
            request.add(
                InvoiceRequest(
                    code = employeeRoute.codFactura,
                    idContador = employeeRoute.id,
                    precio = invoice.totals.totalToPay,
                    fechaEmision = readingFormData.date.toString(),
                    usuarioCreacion = userName.orEmpty(),
                    codEstado = "PEN",
                    lectura = ReadRequest(
                        meterReading = readingFormData.meterReading,
                        description = readingFormData.observations,
                        abnormalConsumption = readingFormData.abnormalConsumption == true
                    ),
                )
            )
        }


        if (request.isEmpty()) return Result.failure()
        val requestResult = invoiceRepository.sendInvoice(request = request)
        if (requestResult.isSuccess) {
            updateReadingFormDataIsSyncedUseCase(requestResult.getOrNull?.allIds.orEmpty())
        }

        return Result.success()
    }
}