package com.codemakers.aquaplus.domain.usecases

import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.repository.AuthRepository
import com.codemakers.aquaplus.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {

    operator fun invoke(
        username: String,
        password: String
    ): Flow<Result<Unit>> = flow {
        try {
            val loginResult = authRepository.login(username, password)
            when (loginResult) {
                is Result.Success -> {
                    val userId = loginResult.data.id
                    loadProfile(userId = userId)
                }
                is Result.Error -> emit(Result.Error(loginResult.error))
                is Result.Exception -> emit(Result.Exception(loginResult.error))
            }
        } catch (e: Exception) {
            emit(Result.Exception(e))
        }
    }

    private suspend fun FlowCollector<Result<Unit>>.loadProfile(userId: Int) {
        val userResult = userRepository.loadProfile(userId = userId)
        when (userResult) {
            is Result.Success -> emit(Result.Success(Unit))
            is Result.Error -> emit(Result.Error(userResult.error))
            is Result.Exception -> emit(Result.Exception(userResult.error))
        }
    }
}