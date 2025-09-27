package com.codemakers.aquaplus.domain.usecases

import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.repository.EmployeeRouteRepository

class UpdateReadingFormDataIsSyncedUseCase(
    private val employeeRouteRepository: EmployeeRouteRepository,
) {

    suspend operator fun invoke(
        employeeRouteIds: List<Int>,
    ): Result<Unit> = try {
        employeeRouteIds.onEach {
            employeeRouteRepository.updateReadingFormDataIsSynced(employeeRouteId = it)
        }
        Result.Success(Unit)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Exception(e)
    }
}