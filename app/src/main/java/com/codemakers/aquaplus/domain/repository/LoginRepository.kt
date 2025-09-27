package com.codemakers.aquaplus.domain.repository

import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.LoginSession

interface LoginRepository {

    suspend fun login(username: String, password: String): Result<LoginSession>
}