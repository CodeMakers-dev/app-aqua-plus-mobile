package com.codemakers.aquaplus.domain.repository

import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.Profile

interface UserRepository {

    suspend fun loadProfile(userId: Int): Result<Profile>

    suspend fun getProfile(): Profile?

    suspend fun cleanProfile()

    suspend fun isUserLogged(): Boolean

    suspend fun recoverPassword(email: String): Result<String?>

    suspend fun updatePassword(token: String, newPassword: String): Result<String?>
}