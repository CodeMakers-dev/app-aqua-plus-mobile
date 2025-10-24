package com.codemakers.aquaplus.domain.usecases

import com.codemakers.aquaplus.domain.repository.EmployeeRouteRepository

class HasUnsyncedReadingFormDataUseCase(
    private val employeeRouteRepository: EmployeeRouteRepository,
) {

    suspend operator fun invoke(): Boolean {
        val unsyncedData = employeeRouteRepository.getAllReadingFormDataForSync()
        val hasUnsynced = unsyncedData.isNotEmpty()
        return hasUnsynced
    }
}
