package com.codemakers.aquaplus.data.extensions

import com.codemakers.aquaplus.domain.common.ConnectError
import com.codemakers.aquaplus.domain.common.ErrorDomain
import com.codemakers.aquaplus.domain.models.ErrorEntity
import com.google.gson.Gson
import com.codemakers.aquaplus.domain.common.ExceptionMapper
import retrofit2.HttpException
import java.io.IOException

fun Exception.toErrorEntity(): ErrorEntity? =
    (this as? HttpException)?.response()?.errorBody()?.charStream()?.let {
        Gson().fromJson(it, ErrorEntity::class.java)
    }

fun Exception.handleExceptionMapper(mapper: ExceptionMapper): ErrorDomain =
    this.toErrorEntity()?.let { mapper.getCustomError(it) } ?: this.toCommonError()
    ?: mapper.getGenericError(this)

fun Exception.toCommonError(): ErrorDomain? = when (this) {
    is IOException -> ConnectError
    else -> null
}