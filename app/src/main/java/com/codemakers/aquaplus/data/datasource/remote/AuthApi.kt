package com.codemakers.aquaplus.data.datasource.remote

import com.codemakers.aquaplus.data.models.request.LoginRequestDto
import com.codemakers.aquaplus.data.models.response.BaseResponse
import com.codemakers.aquaplus.data.models.response.LoginSessionDto
import com.codemakers.aquaplus.data.models.response.TokenDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("api/v1/usuario/validar")
    suspend fun login(
        @Body request: LoginRequestDto,
    ): BaseResponse<LoginSessionDto>

    @POST("api/v1/usuario/auth/refresh")
    suspend fun refreshToken(
        @Header("Authorization") refreshToken: String,
    ): BaseResponse<TokenDto>
}