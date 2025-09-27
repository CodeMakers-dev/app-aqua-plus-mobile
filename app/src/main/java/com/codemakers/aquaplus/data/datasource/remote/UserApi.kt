package com.codemakers.aquaplus.data.datasource.remote

import com.codemakers.aquaplus.data.common.NoAuth
import com.codemakers.aquaplus.data.models.request.NewPasswordRequestDto
import com.codemakers.aquaplus.data.models.response.BaseResponse
import com.codemakers.aquaplus.data.models.response.ProfileDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    @GET("api/v1/usuario/{userId}")
    suspend fun getProfile(
        @Path("userId") userId: Int
    ): BaseResponse<ProfileDto>

    @POST("api/v1/usuario/recoverPassword")
    suspend fun recoverPassword(
        @Query("correo") email: String
    ): BaseResponse<String?>

    @NoAuth
    @POST("api/v1/usuario/update-password")
    suspend fun updatePassword(
        @Header("token") token: String,
        @Body request: NewPasswordRequestDto,
    ): BaseResponse<String?>
}