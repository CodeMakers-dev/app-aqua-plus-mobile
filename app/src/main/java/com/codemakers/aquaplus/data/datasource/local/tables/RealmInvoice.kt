package com.codemakers.aquaplus.data.datasource.local.tables

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey

class RealmInvoice : RealmObject {
    @PrimaryKey
    var employeeRouteId: Int = 0
    @Index
    var personId: Int = 0
    var invoiceJson: String = ""
    var createdAt: Long = System.currentTimeMillis()
}
