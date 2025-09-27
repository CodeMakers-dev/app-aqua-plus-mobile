package com.codemakers.aquaplus.data.common

import com.codemakers.aquaplus.data.extensions.handleExceptionMapper
import com.codemakers.aquaplus.domain.common.ConnectError
import com.codemakers.aquaplus.domain.common.ErrorDomain
import com.codemakers.aquaplus.domain.common.ExceptionMapper
import com.codemakers.aquaplus.domain.common.GenericErrorMapper
import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.provider.NetworkProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.ConnectException

abstract class BaseRepository : KoinComponent {

    private val networkProvider: NetworkProvider by inject()

    protected suspend fun <T> handlerErrorMapper(
        exceptionMapper: ExceptionMapper = GenericErrorMapper,
        errorHandler: (Exception, ErrorDomain) -> Unit = { _, _ -> },
        action: suspend () -> Result<T>
    ) = try {
        if (networkProvider.isAvailable()) {
            action()
        } else {
            val error = ConnectError
            errorHandler(ConnectException(), error)
            Result.Error(ConnectError)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        val error = e.handleExceptionMapper(exceptionMapper)
        errorHandler(e, error)
        Result.Error(error)
    }

    protected suspend fun <T> executeNetworkAction(
        action: suspend () -> T
    ): T = withContext(Dispatchers.IO) { action() }

    protected suspend fun <T> executeMainAction(
        action: suspend () -> T
    ): T = withContext(Dispatchers.Main) { action() }
}