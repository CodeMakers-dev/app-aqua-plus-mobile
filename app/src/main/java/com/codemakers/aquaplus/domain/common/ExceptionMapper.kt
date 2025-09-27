package com.codemakers.aquaplus.domain.common

import com.codemakers.aquaplus.domain.models.ErrorEntity
import java.lang.Exception

interface ExceptionMapper {

    fun getCustomError(errorEntity: ErrorEntity): ErrorDomain

    fun getGenericError(exception: Exception): ErrorDomain
}