package com.codemakers.aquaplus.data.repository

import com.codemakers.aquaplus.data.common.BaseRepository
import com.codemakers.aquaplus.data.datasource.local.dao.EmployeeRouteDao
import com.codemakers.aquaplus.data.datasource.local.dao.ReadingFormDataDao
import com.codemakers.aquaplus.data.datasource.local.tables.toDomain
import com.codemakers.aquaplus.data.datasource.remote.EmployeeRouteApi
import com.codemakers.aquaplus.data.models.response.toDomain
import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.EmployeeRoute
import com.codemakers.aquaplus.domain.models.EmployeeRouteConfig
import com.codemakers.aquaplus.domain.models.ReadingFormData
import com.codemakers.aquaplus.domain.repository.EmployeeRouteRepository
import com.codemakers.aquaplus.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

class EmployeeRouteRepositoryImpl(
    private val userRepository: UserRepository,
    private val employeeRouteApi: EmployeeRouteApi,
    private val employeeRouteDao: EmployeeRouteDao,
    private val readingFormDataDao: ReadingFormDataDao,
) : EmployeeRouteRepository, BaseRepository() {

    override suspend fun loadAllEmployeeRouteFlow(): Result<List<EmployeeRoute>> =
        handlerErrorMapper(
            action = {
                val userId = userRepository.getProfile()?.person?.id ?: 0
                val employeeRouteResult = employeeRouteApi.getEmployeeRoute(userId = userId)
                employeeRouteResult.response.data.onEach { employeeRouteDao.saveNewEmployeeRoute(it) }

                val employeeRouteConfigResult =
                    employeeRouteApi.getEmployeeRouteConfig(userId = userId)
                employeeRouteDao.saveNewEmployeeRouteConfig(employeeRouteConfigResult.response)
                Result.Success(employeeRouteResult.response.data.map { it.empresaClienteContador.toDomain() })
            }
        )

    override suspend fun loadAllEmployeeRouteConfigFlow(): Result<EmployeeRouteConfig> =
        handlerErrorMapper(
            action = {
                val userId = userRepository.getProfile()?.person?.id ?: 0
                val result = employeeRouteApi.getEmployeeRouteConfig(userId = userId)
                employeeRouteDao.saveNewEmployeeRouteConfig(result.response)
                Result.Success(result.response.toDomain())
            }
        )

    override suspend fun getAllEmployeeRouteFlow(): Flow<List<EmployeeRoute>> = flow {
        employeeRouteDao.getAllEmployeeRouteFlow().collect { result ->
            val data = result.list
            emit(data.map { it.toDomain() })
        }
    }

    override suspend fun getAllEmployeeRouteConfigFlow(): Flow<List<EmployeeRouteConfig>> = flow {
        employeeRouteDao.getAllEmployeeRouteConfigFlow().collect { result ->
            val data = result.list
            emit(data.map { it.toDomain() })
        }
    }

    override suspend fun getAllReadingFormDataFlow(): Flow<List<ReadingFormData>> = flow {
        readingFormDataDao.getAllReadingFormDataFlow().collect { result ->
            val data = result.list
            emit(data.map { it.toDomain() })
        }
    }

    override suspend fun getAllReadingFormDataForSync(): List<ReadingFormData> =
        readingFormDataDao.getAllReadingFormDataForSync().map { it.toDomain() }

    override suspend fun getEmployeeRouteByIdFlow(
        employeeRouteId: Int,
    ): EmployeeRoute? =
        employeeRouteDao.getEmployeeRouteByIdFlow(id = employeeRouteId)?.toDomain()

    override suspend fun getEmployeeRouteById(
        employeeRouteId: Int,
    ): EmployeeRoute? = employeeRouteDao.getEmployeeRouteById(id = employeeRouteId)?.toDomain()

    override suspend fun getEmployeeRouteConfigByIdFlow(
        empresaId: Int
    ): EmployeeRouteConfig? =
        employeeRouteDao.getEmployeeRouteConfigByIdFlow(empresaId = empresaId)?.toDomain()

    override suspend fun getEmployeeRouteConfigById(
        empresaId: Int
    ): EmployeeRouteConfig? =
        employeeRouteDao.getEmployeeRouteConfigById(empresaId = empresaId)?.toDomain()

    override suspend fun getReadingFormDataByEmployeeRouteIdFlow(
        employeeRouteId: Int,
    ): ReadingFormData? =
        readingFormDataDao.getReadingFormDataByEmployeeRouteIdFlow(employeeRouteId)?.toDomain()

    override suspend fun getReadingFormDataByEmployeeRouteId(
        employeeRouteId: Int,
    ): ReadingFormData? =
        readingFormDataDao.getReadingFormDataByEmployeeRouteId(employeeRouteId)?.toDomain()

    override suspend fun saveNewReadingFormData(
        employeeRouteId: Int,
        meterReading: String,
        abnormalConsumption: Boolean?,
        observations: String,
        readingFormDataId: Long?,
        date: LocalDate,
    ): Result<Unit> {
        readingFormDataDao.saveNewReadingFormData(
            employeeRouteId = employeeRouteId,
            meterReading = meterReading,
            abnormalConsumption = abnormalConsumption,
            observations = observations,
            readingFormDataId = readingFormDataId,
            date = date,
        )
        return Result.Success(Unit)
    }

    override suspend fun updateReadingFormDataIsSynced(
        employeeRouteId: Int,
    ) {
        readingFormDataDao.updateReadingFormDataIsSynced(employeeRouteId = employeeRouteId)
    }
}