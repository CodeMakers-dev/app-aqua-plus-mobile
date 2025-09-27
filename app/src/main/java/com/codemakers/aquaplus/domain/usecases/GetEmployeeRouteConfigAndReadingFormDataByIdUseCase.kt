package com.codemakers.aquaplus.domain.usecases

import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.EmployeeRouteConfig
import com.codemakers.aquaplus.domain.models.ReadingFormData
import com.codemakers.aquaplus.domain.repository.EmployeeRouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class GetEmployeeRouteConfigAndReadingFormDataByIdUseCase(
    private val employeeRouteRepository: EmployeeRouteRepository,
) {

    operator fun invoke(
        employeeRouteId: Int,
        empresaId: Int
    ): Flow<Result<Pair<EmployeeRouteConfig?, ReadingFormData?>>> = flow {
        try {
            val employeeRouteConfig =
                employeeRouteRepository.getEmployeeRouteConfigByIdFlow(empresaId = empresaId)
                    .first()
            val readingFormData =
                employeeRouteRepository.getReadingFormDataByEmployeeRouteIdFlow(employeeRouteId = employeeRouteId)
                    .first()
            emit(Result.Success(employeeRouteConfig to readingFormData))
        } catch (e: Exception) {
            emit(Result.Exception(e))
        }
    }
}