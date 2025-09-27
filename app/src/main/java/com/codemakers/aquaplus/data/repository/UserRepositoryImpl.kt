package com.codemakers.aquaplus.data.repository

import com.codemakers.aquaplus.data.common.BaseRepository
import com.codemakers.aquaplus.data.datasource.remote.UserApi
import com.codemakers.aquaplus.data.models.request.NewPasswordRequestDto
import com.codemakers.aquaplus.data.models.response.ProfileDto
import com.codemakers.aquaplus.data.models.response.toDomain
import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.Profile
import com.codemakers.aquaplus.domain.repository.PreferencesRepository
import com.codemakers.aquaplus.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val preferencesRepository: PreferencesRepository,
) : UserRepository, BaseRepository() {

    override suspend fun loadProfile(
        userId: Int
    ): Result<Profile> = handlerErrorMapper(
        action = {
            val result = userApi.getProfile(userId)
            preferencesRepository.setObject(USER, result.response)
            Result.Success(result.response.toDomain())
        }
    )

    override suspend fun getProfile(): Profile? =
        preferencesRepository.getObject(USER, ProfileDto::class.java)?.toDomain()

    override suspend fun cleanProfile() {
        preferencesRepository.remove(USER)
        preferencesRepository.remove(TOKEN)
    }

    override suspend fun isUserLogged(): Boolean {
        val token = preferencesRepository.getString(TOKEN)
        return !token.isNullOrBlank()
    }

    override suspend fun recoverPassword(email: String): Result<String?> = handlerErrorMapper(
        action = {
            val result = userApi.recoverPassword(email)
            Result.Success(result.message)
        }
    )

    override suspend fun updatePassword(
        token: String,
        newPassword: String
    ): Result<String?> = handlerErrorMapper(
        action = {
            val newPassword = NewPasswordRequestDto(newPassword)
            val result = userApi.updatePassword(token, newPassword)
            Result.Success(result.message)
        }
    )
}