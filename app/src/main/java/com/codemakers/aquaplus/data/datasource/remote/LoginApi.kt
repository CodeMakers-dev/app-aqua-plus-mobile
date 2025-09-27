package com.codemakers.aquaplus.data.datasource.remote

import com.codemakers.aquaplus.data.models.request.LoginRequestDto
import com.codemakers.aquaplus.data.models.response.BaseResponse
import com.codemakers.aquaplus.data.models.response.LoginSessionDto
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("api/v1/usuario/validar")
    suspend fun login(
        @Body request: LoginRequestDto,
    ): BaseResponse<LoginSessionDto>
}