package com.codemakers.aquaplus.domain.usecases

import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.EmployeeRoute
import com.codemakers.aquaplus.domain.models.EmployeeRouteConfig
import com.codemakers.aquaplus.domain.repository.EmployeeRouteRepository

class GetEmployeeRouteAndConfigByIdUseCase(
    private val employeeRouteRepository: EmployeeRouteRepository,
) {

    suspend operator fun invoke(
        employeeRouteId: Int,
    ): Result<Pair<EmployeeRoute?, EmployeeRouteConfig?>> = try {
        val employeeRoute = employeeRouteRepository.getEmployeeRouteById(employeeRouteId = employeeRouteId)
        val employeeRouteConfig = employeeRouteRepository.getEmployeeRouteConfigById(empresaId = employeeRoute?.empresa?.id ?: 0)
        Result.Success(employeeRoute to employeeRouteConfig)
    } catch (e: Exception) {
        Result.Exception(e)
    }
}