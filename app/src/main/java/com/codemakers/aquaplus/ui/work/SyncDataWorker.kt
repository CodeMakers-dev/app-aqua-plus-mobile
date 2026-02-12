package com.codemakers.aquaplus.ui.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.codemakers.aquaplus.data.datasource.local.dao.AuthSessionDao
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
    private val authSessionDao: AuthSessionDao,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val userName = userRepository.getProfile()?.username

        val syncResult = getAllReadingFormDataForSyncUseCase()
        val data = syncResult.getOrNull
        if (data.isNullOrEmpty()) return Result.failure()

        val groupedByPerson = data.groupBy { it.personId }

        var hasAnySuccess = false

        groupedByPerson.forEach { (personId, readingFormDataList) ->
            val authSession = authSessionDao.getAuthSession(personId)
            val token = authSession?.token ?: return@forEach

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

            if (request.isEmpty()) return@forEach

            val requestResult = invoiceRepository.sendInvoice(request = request, token = token)
            if (requestResult.isSuccess) {
                updateReadingFormDataIsSyncedUseCase(employeeRouteIds)
                hasAnySuccess = true
            }
        }

        return if (hasAnySuccess) Result.success() else Result.failure()
    }
}