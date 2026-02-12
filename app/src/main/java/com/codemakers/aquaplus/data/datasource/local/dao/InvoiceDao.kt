package com.codemakers.aquaplus.data.datasource.local.dao

import com.codemakers.aquaplus.data.datasource.local.tables.RealmInvoice
import com.codemakers.aquaplus.ui.models.Invoice
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class InvoiceDao(private val realm: Realm) {

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()

    suspend fun saveInvoice(employeeRouteId: Int, invoice: Invoice, personId: Int) = withContext(Dispatchers.IO) {
        realm.write {
            val realmInvoice = query<RealmInvoice>("employeeRouteId == $0 AND personId == $1", employeeRouteId, personId)
                .first()
                .find()

            val jsonString = gson.toJson(invoice)

            if (realmInvoice != null) {
                // Update existing
                realmInvoice.invoiceJson = jsonString
                realmInvoice.createdAt = System.currentTimeMillis()
            } else {
                // Create new
                copyToRealm(RealmInvoice().apply {
                    this.employeeRouteId = employeeRouteId
                    this.personId = personId
                    this.invoiceJson = jsonString
                    this.createdAt = System.currentTimeMillis()
                })
            }
        }
    }

    suspend fun getInvoiceByEmployeeRouteId(employeeRouteId: Int, personId: Int): Invoice? = withContext(Dispatchers.IO) {
        val realmInvoice = realm.query<RealmInvoice>("employeeRouteId == $0 AND personId == $1", employeeRouteId, personId)
            .first()
            .find()

        realmInvoice?.let {
            try {
                gson.fromJson(it.invoiceJson, Invoice::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun deleteInvoice(employeeRouteId: Int, personId: Int) = withContext(Dispatchers.IO) {
        realm.write {
            val invoice = query<RealmInvoice>("employeeRouteId == $0 AND personId == $1", employeeRouteId, personId)
                .first()
                .find()
            invoice?.let { delete(it) }
        }
    }
}
