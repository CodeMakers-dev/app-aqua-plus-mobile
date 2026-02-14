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

    private suspend fun getPersonId(): Int = userRepository.getProfile()?.person?.id ?: 0

    override suspend fun loadAllEmployeeRouteFlow(): Result<List<EmployeeRoute>> =
        handlerErrorMapper(
            action = {
                val personId = getPersonId()
                val apiPersonId = userRepository.getProfile()?.person?.id ?: 0
                val employeeRouteResult = employeeRouteApi.getEmployeeRoute(userId = apiPersonId)
                employeeRouteResult.response.data.onEach { employeeRouteDao.saveNewEmployeeRoute(it, personId = personId) }

                val employeeRouteConfigResult =
                    employeeRouteApi.getEmployeeRouteConfig(userId = apiPersonId)
                employeeRouteDao.saveNewEmployeeRouteConfig(employeeRouteConfigResult.response, personId = personId)
                Result.Success(employeeRouteResult.response.data.map { it.empresaClienteContador.toDomain() })
            }
        )

    override suspend fun loadAllEmployeeRouteConfigFlow(): Result<EmployeeRouteConfig> =
        handlerErrorMapper(
            action = {
                val personId = getPersonId()
                val apiPersonId = userRepository.getProfile()?.person?.id ?: 0
                val result = employeeRouteApi.getEmployeeRouteConfig(userId = apiPersonId)
                employeeRouteDao.saveNewEmployeeRouteConfig(result.response, personId = personId)
                Result.Success(result.response.toDomain())
            }
        )

    override suspend fun getAllEmployeeRouteFlow(): Flow<List<EmployeeRoute>> = flow {
        val personId = getPersonId()
        employeeRouteDao.getAllEmployeeRouteFlow(personId = personId).collect { result ->
            val data = result.list
            emit(data.map { it.toDomain() })
        }
    }

    override suspend fun getAllEmployeeRouteConfigFlow(): Flow<List<EmployeeRouteConfig>> = flow {
        val personId = getPersonId()
        employeeRouteDao.getAllEmployeeRouteConfigFlow(personId = personId).collect { result ->
            val data = result.list
            emit(data.map { it.toDomain() })
        }
    }

    override suspend fun getAllReadingFormDataFlow(): Flow<List<ReadingFormData>> = flow {
        val personId = getPersonId()
        readingFormDataDao.getAllReadingFormDataFlow(personId = personId).collect { result ->
            val data = result.list
            emit(data.map { it.toDomain() })
        }
    }

    override suspend fun getAllReadingFormDataForSync(): List<ReadingFormData> {
        return readingFormDataDao.getAllReadingFormDataForSync().map { it.toDomain() }
    }

    override suspend fun getEmployeeRouteByIdFlow(
        employeeRouteId: Int,
    ): EmployeeRoute? {
        val personId = getPersonId()
        return employeeRouteDao.getEmployeeRouteByIdFlow(id = employeeRouteId, personId = personId)?.toDomain()
    }

    override suspend fun getEmployeeRouteById(
        employeeRouteId: Int,
        personId: Int?,
    ): EmployeeRoute? {
        val resolvedPersonId = personId ?: getPersonId()
        return employeeRouteDao.getEmployeeRouteById(id = employeeRouteId, personId = resolvedPersonId)?.toDomain()
    }

    override suspend fun getEmployeeRouteConfigByIdFlow(
        empresaId: Int
    ): EmployeeRouteConfig? {
        val personId = getPersonId()
        return employeeRouteDao.getEmployeeRouteConfigByIdFlow(empresaId = empresaId, personId = personId)?.toDomain()
    }

    override suspend fun getEmployeeRouteConfigById(
        empresaId: Int,
        personId: Int?,
    ): EmployeeRouteConfig? {
        val resolvedPersonId = personId ?: getPersonId()
        return employeeRouteDao.getEmployeeRouteConfigById(empresaId = empresaId, personId = resolvedPersonId)?.toDomain()
    }

    override suspend fun getReadingFormDataByEmployeeRouteIdFlow(
        employeeRouteId: Int,
    ): ReadingFormData? {
        val personId = getPersonId()
        return readingFormDataDao.getReadingFormDataByEmployeeRouteIdFlow(employeeRouteId, personId = personId)?.toDomain()
    }

    override suspend fun getReadingFormDataByEmployeeRouteId(
        employeeRouteId: Int,
    ): ReadingFormData? {
        val personId = getPersonId()
        return readingFormDataDao.getReadingFormDataByEmployeeRouteId(employeeRouteId, personId = personId)?.toDomain()
    }

    override suspend fun saveNewReadingFormData(
        employeeRouteId: Int,
        meterReading: String,
        abnormalConsumption: Boolean?,
        observations: String,
        readingFormDataId: Long?,
        date: LocalDate,
        serial: String,
    ): Result<Unit> {
        val personId = getPersonId()
        readingFormDataDao.saveNewReadingFormData(
            employeeRouteId = employeeRouteId,
            meterReading = meterReading,
            abnormalConsumption = abnormalConsumption,
            observations = observations,
            readingFormDataId = readingFormDataId,
            date = date,
            personId = personId,
            serial = serial,
        )
        return Result.Success(Unit)
    }

    override suspend fun updateReadingFormDataIsSynced(
        employeeRouteId: Int,
    ) {
        val personId = getPersonId()
        readingFormDataDao.updateReadingFormDataIsSynced(employeeRouteId = employeeRouteId, personId = personId)
    }
}