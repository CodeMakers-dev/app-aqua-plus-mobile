package com.codemakers.aquaplus.domain.usecases

import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.EmployeeRoute
import com.codemakers.aquaplus.domain.models.EmployeeRouteConfig
import com.codemakers.aquaplus.domain.repository.EmployeeRouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetEmployeeRouteByIdUseCase(
    private val employeeRouteRepository: EmployeeRouteRepository,
) {

    operator fun invoke(
        employeeRouteId: Int,
    ): Flow<Result<Pair<EmployeeRoute?, EmployeeRouteConfig?>>> = flow {
        try {
            val employeeRoute =
                employeeRouteRepository.getEmployeeRouteByIdFlow(employeeRouteId = employeeRouteId)
            val empresaId = employeeRoute?.empresa?.id ?: 0
            val employeeRouteConfig =
                employeeRouteRepository.getEmployeeRouteConfigByIdFlow(empresaId = empresaId)
            emit(Result.Success(employeeRoute to employeeRouteConfig))
        } catch (e: Exception) {
            emit(Result.Exception(e))
        }
    }
}