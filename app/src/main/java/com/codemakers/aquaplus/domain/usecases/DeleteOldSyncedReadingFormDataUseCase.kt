package com.codemakers.aquaplus.domain.usecases

import com.codemakers.aquaplus.domain.repository.EmployeeRouteRepository

class DeleteOldSyncedReadingFormDataUseCase(
    private val employeeRouteRepository: EmployeeRouteRepository,
) {

    suspend operator fun invoke() {
        employeeRouteRepository.deleteOldSyncedReadingFormData()
    }
}
