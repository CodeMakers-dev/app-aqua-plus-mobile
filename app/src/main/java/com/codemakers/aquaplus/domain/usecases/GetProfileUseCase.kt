package com.codemakers.aquaplus.domain.usecases

import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.Profile
import com.codemakers.aquaplus.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetProfileUseCase(
    private val userRepository: UserRepository,
) {

    operator fun invoke(): Flow<Result<Profile?>> = flow {
        val profile = userRepository.getProfile()
        emit(Result.Success(profile))
    }
}