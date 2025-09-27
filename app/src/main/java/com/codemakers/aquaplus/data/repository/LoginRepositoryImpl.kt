package com.codemakers.aquaplus.data.repository

import com.codemakers.aquaplus.data.common.BaseRepository
import com.codemakers.aquaplus.data.datasource.remote.LoginApi
import com.codemakers.aquaplus.data.models.request.LoginRequestDto
import com.codemakers.aquaplus.data.models.response.toDomain
import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.LoginSession
import com.codemakers.aquaplus.domain.repository.LoginRepository
import com.codemakers.aquaplus.domain.repository.PreferencesRepository

class LoginRepositoryImpl(
    private val loginApi: LoginApi,
    private val preferencesRepository: PreferencesRepository,
) : LoginRepository, BaseRepository() {

    override suspend fun login(
        username: String,
        password: String
    ): Result<LoginSession> = handlerErrorMapper(
        errorHandler = { _, _ ->
            preferencesRepository.remove(TOKEN)
            preferencesRepository.remove(USER)
        },
        action = {
            val response = loginApi.login(LoginRequestDto(username, password))
            preferencesRepository.setString(TOKEN, response.response.token)
            Result.Success(response.response.toDomain())
        }
    )
}