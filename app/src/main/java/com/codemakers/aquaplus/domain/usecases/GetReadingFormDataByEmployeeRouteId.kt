package com.codemakers.aquaplus.domain.usecases

import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.ReadingFormData
import com.codemakers.aquaplus.domain.repository.EmployeeRouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetReadingFormDataByEmployeeRouteId(
    private val employeeRouteRepository: EmployeeRouteRepository,
) {

    operator fun invoke(
        employeeRouteId: Int,
    ): Flow<Result<ReadingFormData?>> = flow {
        try {
            val result = employeeRouteRepository.getReadingFormDataByEmployeeRouteIdFlow(employeeRouteId)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Exception(e))
        }
    }
}