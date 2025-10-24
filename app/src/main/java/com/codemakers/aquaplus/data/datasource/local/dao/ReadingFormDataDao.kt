package com.codemakers.aquaplus.data.datasource.local.dao

import com.codemakers.aquaplus.data.datasource.local.tables.RealmReadingFormData
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.notifications.SingleQueryChange
import io.realm.kotlin.query.max
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDate

class ReadingFormDataDao(
    private val realm: Realm,
) {

    suspend fun getReadingFormDataByEmployeeRouteIdFlow(
        employeeRouteId: Int,
    ): Flow<SingleQueryChange<RealmReadingFormData>> {
        return withContext(Dispatchers.IO) {
            realm.query<RealmReadingFormData>("employeeRouteId = $0", employeeRouteId)
                .first()
                .asFlow()
        }
    }

    suspend fun getReadingFormDataByEmployeeRouteId(
        employeeRouteId: Int,
    ): RealmReadingFormData? {
        return withContext(Dispatchers.IO) {
            realm.query<RealmReadingFormData>("employeeRouteId = $0", employeeRouteId)
                .first().find()
        }
    }

    suspend fun getAllReadingFormDataFlow(): Flow<ResultsChange<RealmReadingFormData>> {
        return withContext(Dispatchers.IO) {
            realm.query<RealmReadingFormData>().find().asFlow()
        }
    }

    suspend fun getAllReadingFormDataForSync(): List<RealmReadingFormData> {
        return withContext(Dispatchers.IO) {
            realm.query<RealmReadingFormData>("isSynced == $0", false).find()
        }
    }

    suspend fun updateReadingFormDataIsSynced(
        employeeRouteId: Int,
    ) {
        realm.write {
            val dataToUpDate =
                query<RealmReadingFormData>("employeeRouteId = $0", employeeRouteId)
                    .first()
                    .find()
            if (dataToUpDate != null) {
                dataToUpDate.isSynced = true
            }
        }
    }

    suspend fun saveNewReadingFormData(
        employeeRouteId: Int,
        meterReading: String,
        abnormalConsumption: Boolean?,
        observations: String,
        readingFormDataId: Long?,
        date: LocalDate,
    ) {
        val nextId = readingFormDataId ?: getNextReadingFormDataId()
        realm.write {
            val readingFormData = RealmReadingFormData().apply {
                this.id = nextId
                this.employeeRouteId = employeeRouteId
                this.meterReading = meterReading
                this.abnormalConsumption = abnormalConsumption
                this.observations = observations
                this.dateEpochDay = date.toEpochDay()
                this.isSynced = false
            }
            copyToRealm(readingFormData, updatePolicy = UpdatePolicy.ALL)
        }
    }

    private fun getNextReadingFormDataId(): Long {
        val maxId = realm.query<RealmReadingFormData>()
            .max<Long>("id")
            .find()
        return (maxId ?: 0L) + 1L
    }
}