package com.codemakers.aquaplus.domain.usecases

import com.codemakers.aquaplus.data.datasource.local.dao.AuthSessionDao
import com.codemakers.aquaplus.domain.common.JWTValidator
import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.InvoiceRequest
import com.codemakers.aquaplus.domain.models.InvoiceResponse
import com.codemakers.aquaplus.domain.repository.AuthRepository
import com.codemakers.aquaplus.domain.repository.InvoiceRepository

class SendInvoiceUseCase(
    private val invoiceRepository: InvoiceRepository,
    private val authSessionDao: AuthSessionDao,
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(
        personId: Int,
        request: List<InvoiceRequest>,
    ): Result<InvoiceResponse> =
        try {
            val authSession = authSessionDao.getAuthSession(personId)
            val token = authSession?.token ?: return Result.Exception(Exception("No token found"))
            val refreshToken = authSession?.refreshToken
                ?: return Result.Exception(Exception("No refresh token found"))
            val userName = authSession?.username.orEmpty()

            val jwtValidator = JWTValidator(token.replace("Bearer ", ""))
            val finalToken = if (jwtValidator.isTokenExpiredWithBuffer()) {
                var refreshedToken: String? = null
                var attempts = 0
                val maxAttempts = 3

                while (attempts < maxAttempts && refreshedToken == null) {
                    attempts++
                    val result = authRepository.refreshToken(refreshToken)

                    when (result) {
                        is Result.Success -> {
                            refreshedToken = result.data.token
                        }

                        else -> {
                            if (attempts >= maxAttempts) {
                                return Result.Exception(Exception("Failed to refresh token after $maxAttempts attempts"))
                            }
                        }
                    }
                }

                refreshedToken ?: return Result.Exception(Exception("Failed to refresh token"))
            } else {
                token
            }

            val newRequest = request.map {
                it.copy(
                    usuarioCreacion = userName
                )
            }
            val result = invoiceRepository.sendInvoice(
                request = newRequest,
                token = finalToken
            )
            when (result) {
                is Result.Success -> Result.Success(result.data)
                is Result.Error -> Result.Error(result.error)
                is Result.Exception -> Result.Exception(result.error)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Exception(e)
        }
}