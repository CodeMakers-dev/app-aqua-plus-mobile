package com.codemakers.aquaplus.domain.repository

import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.LoginSession
import com.codemakers.aquaplus.domain.models.Token

interface AuthRepository {

    suspend fun login(username: String, password: String): Result<LoginSession>

    suspend fun refreshToken(refreshToken: String): Result<Token>
}