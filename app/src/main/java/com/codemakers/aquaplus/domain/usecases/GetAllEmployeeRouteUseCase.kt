package com.codemakers.aquaplus.domain.usecases

import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.EmployeeRoute
import com.codemakers.aquaplus.domain.repository.EmployeeRouteRepository

class GetAllEmployeeRouteUseCase(
    private val employeeRouteRepository: EmployeeRouteRepository,
) {

    suspend operator fun invoke(): Result<List<EmployeeRoute>> =
        try {
            val result = employeeRouteRepository.getAllEmployeeRouteFlow()
            Result.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Exception(e)
        }
    }