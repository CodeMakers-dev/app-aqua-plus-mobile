package com.codemakers.aquaplus.domain.common

import com.codemakers.aquaplus.domain.common.GenericError.GenericServiceError
import com.codemakers.aquaplus.domain.models.ErrorEntity

object GenericErrorMapper : ExceptionMapper {

    override fun getCustomError(errorEntity: ErrorEntity) = GenericServiceError(
        message = errorEntity.message,
        code = errorEntity.code,
    )

    override fun getGenericError(
        exception: Exception
    ) = GenericServiceError(
        message = exception.message,
    )
}

sealed class GenericError : ErrorDomain() {

    object NotFoundError : GenericError()

    data class GenericServiceError(
        override val message: String? = null,
        override val code: Int? = null,
    ) : GenericError()
}