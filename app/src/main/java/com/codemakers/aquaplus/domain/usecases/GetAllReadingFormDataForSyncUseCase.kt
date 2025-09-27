package com.codemakers.aquaplus.domain.usecases

import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.ReadingFormData
import com.codemakers.aquaplus.domain.repository.EmployeeRouteRepository

class GetAllReadingFormDataForSyncUseCase(
    private val employeeRouteRepository: EmployeeRouteRepository,
) {

    suspend operator fun invoke(): Result<List<ReadingFormData>> = try {
        val result = employeeRouteRepository.getAllReadingFormDataForSync()
        Result.Success(result)
    } catch (e: Exception) {
        Result.Exception(e)
    }
}