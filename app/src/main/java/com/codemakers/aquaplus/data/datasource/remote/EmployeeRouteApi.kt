package com.codemakers.aquaplus.data.datasource.remote

import com.codemakers.aquaplus.data.models.response.BaseResponse
import com.codemakers.aquaplus.data.models.response.EmployeeRouteConfigDto
import com.codemakers.aquaplus.data.models.response.EmployeeRouteDto
import retrofit2.http.GET
import retrofit2.http.Path

interface EmployeeRouteApi {

    @GET("api/v1/ruta-empleado/sync/{employeeId}")
    suspend fun getEmployeeRoute(
        @Path("employeeId") userId: Int,
    ): BaseResponse<EmployeeRouteDto>

    @GET("api/v1/ruta-empleado/config/{employeeId}")
    suspend fun getEmployeeRouteConfig(
        @Path("employeeId") userId: Int,
    ): BaseResponse<EmployeeRouteConfigDto>
}