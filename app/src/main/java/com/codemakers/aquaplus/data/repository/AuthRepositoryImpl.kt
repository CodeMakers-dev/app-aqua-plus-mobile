package com.codemakers.aquaplus.data.repository

import com.codemakers.aquaplus.data.common.BaseRepository
import com.codemakers.aquaplus.data.datasource.remote.AuthApi
import com.codemakers.aquaplus.data.models.request.LoginRequestDto
import com.codemakers.aquaplus.data.models.response.toDomain
import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.LoginSession
import com.codemakers.aquaplus.domain.models.Token
import com.codemakers.aquaplus.domain.repository.AuthRepository
import com.codemakers.aquaplus.domain.repository.PreferencesRepository

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val preferencesRepository: PreferencesRepository,
) : AuthRepository, BaseRepository() {

    override suspend fun login(
        username: String,
        password: String
    ): Result<LoginSession> = handlerErrorMapper(
        errorHandler = { _, _ ->
            preferencesRepository.remove(REFRESH_TOKEN)
            preferencesRepository.remove(TOKEN)
            preferencesRepository.remove(USER)
        },
        action = {
            val response = authApi.login(LoginRequestDto(username, password))
            preferencesRepository.setString(TOKEN, response.response.token)
            preferencesRepository.setString(REFRESH_TOKEN, response.response.refreshToken)
            Result.Success(response.response.toDomain())
        }
    )

    override suspend fun refreshToken(
        refreshToken: String
    ): Result<Token> = handlerErrorMapper(
        errorHandler = { _, _ ->
            preferencesRepository.remove(REFRESH_TOKEN)
        },
        action = {
            val response = authApi.refreshToken(refreshToken)
            preferencesRepository.setString(REFRESH_TOKEN, response.response.token)
            Result.Success(response.response.toDomain())
        }
    )
}