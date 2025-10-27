package com.codemakers.aquaplus.ui.modules.home.features.route

import com.codemakers.aquaplus.domain.models.EmployeeRoute
import com.codemakers.aquaplus.domain.models.ReadingFormData
import com.codemakers.aquaplus.ui.extensions.isAfterToday
import com.codemakers.aquaplus.ui.extensions.isToday

data class RouteUiState(
    val isLoading: Boolean = true,
    val error: String? = null,

    val allRoutes: List<EmployeeRoute>? = null,
    val allData: List<ReadingFormData> = emptyList(),

    val routes: List<EmployeeRoute> = emptyList(),

    val search: String = "",
) {

    fun isInvoiceAvailable(employeeRouteId: Int): Boolean =
        allData.any { it.employeeRouteId == employeeRouteId }

    private fun isToday(employeeRouteId: Int): Boolean =
        allData.find { it.employeeRouteId == employeeRouteId }?.date?.isToday == true

    fun isSynced(employeeRouteId: Int): Boolean =
        allData.find { it.employeeRouteId == employeeRouteId }?.isSynced ?: false

    val pendingRoutes: List<EmployeeRoute>
        get() = routes.filter { !isInvoiceAvailable(it.id) }

    val completedRoutes: List<EmployeeRoute>
        get() = routes.filter {
            isInvoiceAvailable(it.id) &&
                    (!isSynced(it.id) || !isToday(it.id))
        }

}
