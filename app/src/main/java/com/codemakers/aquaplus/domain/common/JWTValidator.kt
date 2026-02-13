package com.codemakers.aquaplus.domain.common

import com.auth0.android.jwt.JWT
import java.util.Date

class JWTValidator(private val token: String) {
    
    private val jwt: JWT = JWT(token)
    
    val isTokenExpired: Boolean
        get() {
            val expiresAt = jwt.expiresAt ?: return true
            return expiresAt.before(Date())
        }
    
    fun isTokenExpiredWithBuffer(bufferSeconds: Long = 60): Boolean {
        val expiresAt = jwt.expiresAt ?: return true
        val currentTime = Date()
        val bufferTime = Date(currentTime.time + (bufferSeconds * 1000))
        return expiresAt.before(bufferTime)
    }
}
