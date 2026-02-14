package com.codemakers.aquaplus.domain.repository

import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.EmployeeRoute
import com.codemakers.aquaplus.domain.models.EmployeeRouteConfig
import com.codemakers.aquaplus.domain.models.ReadingFormData
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface EmployeeRouteRepository {

    suspend fun loadAllEmployeeRouteFlow(): Result<List<EmployeeRoute>>

    suspend fun loadAllEmployeeRouteConfigFlow(): Result<EmployeeRouteConfig>

    suspend fun getAllEmployeeRouteFlow(): Flow<List<EmployeeRoute>>

    suspend fun getAllEmployeeRouteConfigFlow(): Flow<List<EmployeeRouteConfig>>

    suspend fun getAllReadingFormDataFlow(): Flow<List<ReadingFormData>>

    suspend fun getAllReadingFormDataForSync(): List<ReadingFormData>

    suspend fun getEmployeeRouteByIdFlow(
        employeeRouteId: Int,
    ): EmployeeRoute?

    suspend fun getEmployeeRouteById(
        employeeRouteId: Int,
        personId: Int? = null,
    ): EmployeeRoute?

    suspend fun getEmployeeRouteConfigByIdFlow(
        empresaId: Int
    ): EmployeeRouteConfig?

    suspend fun getEmployeeRouteConfigById(
        empresaId: Int,
        personId: Int? = null,
    ): EmployeeRouteConfig?

    suspend fun getReadingFormDataByEmployeeRouteIdFlow(
        employeeRouteId: Int,
    ): ReadingFormData?

    suspend fun getReadingFormDataByEmployeeRouteId(
        employeeRouteId: Int,
    ): ReadingFormData?

    suspend fun saveNewReadingFormData(
        employeeRouteId: Int,
        meterReading: String,
        abnormalConsumption: Boolean?,
        observations: String,
        readingFormDataId: Long?,
        date: LocalDate,
        serial: String,
    ): Result<Unit>

    suspend fun updateReadingFormDataIsSynced(
        employeeRouteId: Int,
    )
}