package com.codemakers.aquaplus.data.datasource.local.tables

import com.codemakers.aquaplus.domain.models.ReadingFormData
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import java.time.LocalDate

open class RealmReadingFormData : RealmObject {
    @PrimaryKey
    var id: Long = 0
    
    @Index
    var personId: Int = 0

    @Index
    var employeeRouteId: Int = 0
    
    var meterReading: String = ""
    var abnormalConsumption: Boolean? = null
    var observations: String = ""
    var serial: String = ""
    var dateEpochDay: Long = 0L
    
    @Index
    var isSynced: Boolean = false
}

fun RealmReadingFormData.toDomain(): ReadingFormData = ReadingFormData(
    id = id,
    personId = personId,
    employeeRouteId = employeeRouteId,
    meterReading = meterReading,
    abnormalConsumption = abnormalConsumption,
    observations = observations,
    date = LocalDate.ofEpochDay(dateEpochDay),
    isSynced = isSynced,
    serial = serial,
)