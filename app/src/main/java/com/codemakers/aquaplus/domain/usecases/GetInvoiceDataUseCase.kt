package com.codemakers.aquaplus.domain.usecases

import com.codemakers.aquaplus.domain.repository.EmployeeRouteRepository
import com.codemakers.aquaplus.ui.models.Invoice

class GetInvoiceDataUseCase(
    private val employeeRouteRepository: EmployeeRouteRepository,
    private val getEmployeeRouteAndConfigByIdUseCase: GetEmployeeRouteAndConfigByIdUseCase,
) {

    suspend operator fun invoke(
        employeeRouteId: Int,
    ): Invoice? {
        val readingFormData = employeeRouteRepository
            .getReadingFormDataByEmployeeRouteId(employeeRouteId = employeeRouteId)
        if (readingFormData == null) return null

        val result = getEmployeeRouteAndConfigByIdUseCase(employeeRouteId = employeeRouteId)
        val employeeRouteAndConfig = result.getOrNull
        val employeeRoute = employeeRouteAndConfig?.first
        val employeeRouteConfig = employeeRouteAndConfig?.second

        if (employeeRoute == null || employeeRouteConfig == null) return null

        val invoice = Invoice(
            route = employeeRoute,
            config = employeeRouteConfig,
            data = readingFormData
        )

        return invoice
    }
}