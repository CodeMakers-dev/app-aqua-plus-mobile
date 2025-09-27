package com.codemakers.aquaplus.domain.usecases

import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.ReadingFormData
import com.codemakers.aquaplus.domain.repository.EmployeeRouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAllReadingFormDataUseCase(
    private val employeeRouteRepository: EmployeeRouteRepository,
) {

    operator fun invoke(): Flow<Result<List<ReadingFormData>>> = flow {
        try {
            val result = employeeRouteRepository.getAllReadingFormDataFlow()
            result.collect {
                emit(Result.Success(it))
            }
        } catch (e: Exception) {
            emit(Result.Exception(e))
        }
    }
}