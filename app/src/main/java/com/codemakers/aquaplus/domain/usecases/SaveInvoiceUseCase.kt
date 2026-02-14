package com.codemakers.aquaplus.domain.usecases

import com.codemakers.aquaplus.data.datasource.local.dao.InvoiceDao
import com.codemakers.aquaplus.domain.repository.EmployeeRouteRepository
import com.codemakers.aquaplus.domain.repository.UserRepository
import com.codemakers.aquaplus.ui.models.Invoice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class SaveInvoiceUseCase(
    private val employeeRouteRepository: EmployeeRouteRepository,
    private val invoiceDao: InvoiceDao,
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(employeeRouteId: Int): Invoice? = withContext(Dispatchers.IO) {
        // Generate invoice from current data
        val readingFormDataDeferred = async {
            employeeRouteRepository.getReadingFormDataByEmployeeRouteId(employeeRouteId = employeeRouteId)
        }
        
        val employeeRouteDeferred = async {
            employeeRouteRepository.getEmployeeRouteById(employeeRouteId = employeeRouteId)
        }
        
        val readingFormData = readingFormDataDeferred.await()
        if (readingFormData == null) return@withContext null
        
        val employeeRoute = employeeRouteDeferred.await()
        if (employeeRoute == null) return@withContext null
        
        val employeeRouteConfig = employeeRouteRepository.getEmployeeRouteConfigById(
            empresaId = employeeRoute.empresa?.id ?: 0
        )
        
        if (employeeRouteConfig == null) return@withContext null

        val invoice = Invoice(
            route = employeeRoute,
            config = employeeRouteConfig,
            data = readingFormData
        )
        
        // Save invoice to cache
        val personId = userRepository.getProfile()?.person?.id ?: 0
        invoiceDao.saveInvoice(employeeRouteId, invoice, personId = personId)
        
        invoice
    }
}
