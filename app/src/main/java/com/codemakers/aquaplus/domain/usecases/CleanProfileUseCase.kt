package com.codemakers.aquaplus.domain.usecases

import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CleanProfileUseCase(
    private val userRepository: UserRepository,
) {

    operator fun invoke(): Flow<Result<Unit>> = flow {
        userRepository.cleanProfile()
        emit(Result.Success(Unit))
    }
}