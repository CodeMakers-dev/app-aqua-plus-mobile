package com.codemakers.aquaplus.domain.usecases

import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.EmployeeRoute
import com.codemakers.aquaplus.domain.repository.EmployeeRouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoadAllEmployeeRouteUseCase(
    private val employeeRouteRepository: EmployeeRouteRepository,
) {

    operator fun invoke(): Flow<Result<List<EmployeeRoute>>> = flow {
        try {
            val result = employeeRouteRepository.loadAllEmployeeRouteFlow()
            when (result) {
                is Result.Success -> emit(Result.Success(result.data))
                is Result.Error -> emit(Result.Error(result.error))
                is Result.Exception -> emit(Result.Exception(result.error))
            }
        } catch (e: Exception) {
            emit(Result.Exception(e))
        }
    }
}