package com.codemakers.aquaplus.domain.usecases

import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.ReadingFormData
import com.codemakers.aquaplus.domain.repository.EmployeeRouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

class CreateOrUpdateReadingFormData(
    private val employeeRouteRepository: EmployeeRouteRepository,
) {

    operator fun invoke(
        employeeRouteId: Int,
        meterReading: String,
        abnormalConsumption: Boolean?,
        observations: String,
        readingFormDataId: Long?,
        date: LocalDate,
    ): Flow<Result<ReadingFormData?>> = flow {
        employeeRouteRepository.saveNewReadingFormData(
            employeeRouteId = employeeRouteId,
            meterReading = meterReading,
            abnormalConsumption = abnormalConsumption,
            observations = observations,
            readingFormDataId = readingFormDataId,
            date = date,
        )
        val result = employeeRouteRepository.getReadingFormDataByEmployeeRouteIdFlow(employeeRouteId)
        result.collect {
            emit(Result.Success(it))
        }
    }
}