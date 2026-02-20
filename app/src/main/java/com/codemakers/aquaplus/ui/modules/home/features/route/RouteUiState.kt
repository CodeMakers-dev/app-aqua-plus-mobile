package com.codemakers.aquaplus.ui.modules.home.features.route

import com.codemakers.aquaplus.domain.models.EmployeeRoute
import com.codemakers.aquaplus.domain.models.ReadingFormData
import com.codemakers.aquaplus.ui.extensions.isToday

data class RouteUiState(
    val isLoading: Boolean = true,
    val error: String? = null,

    val allRoutes: List<EmployeeRoute>? = null,
    val allData: List<ReadingFormData> = emptyList(),

    val routes: List<EmployeeRoute> = emptyList(),

    val search: String = "",
) {

    private val allDataByRouteId: Map<Int, ReadingFormData> by lazy {
        allData.associateBy { it.employeeRouteId }
    }

    fun isInvoiceAvailable(employeeRouteId: Int): Boolean =
        allDataByRouteId.containsKey(employeeRouteId)

    fun getContadorSerial(employeeRouteId: Int): String? =
        allDataByRouteId[employeeRouteId]?.serial

    private fun isToday(employeeRouteId: Int): Boolean =
        allDataByRouteId[employeeRouteId]?.date?.isToday == true

    fun isSynced(employeeRouteId: Int): Boolean =
        allDataByRouteId[employeeRouteId]?.isSynced ?: false

    val pendingRoutes: List<EmployeeRoute>
        get() = routes.filter { !isInvoiceAvailable(it.id) }

    val completedRoutes: List<EmployeeRoute>
        get() = routes.filter {
            isInvoiceAvailable(it.id)
                    && isToday(it.id)
        }

}
