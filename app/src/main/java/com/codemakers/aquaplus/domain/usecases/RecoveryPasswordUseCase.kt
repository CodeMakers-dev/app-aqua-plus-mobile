package com.codemakers.aquaplus.domain.usecases

import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecoveryPasswordUseCase(
    private val userRepository: UserRepository,
) {

    operator fun invoke(
        email: String
    ): Flow<Result<String?>> = flow {
        try {
            val userResult = userRepository.recoverPassword(email = email)
            when (userResult) {
                is Result.Success -> emit(Result.Success(userResult.data))
                is Result.Error -> emit(Result.Error(userResult.error))
                is Result.Exception -> emit(Result.Exception(userResult.error))
            }
        } catch (e: Exception) {
            emit(Result.Exception(e))
        }
    }
}