package com.codemakers.aquaplus.domain.common

sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: ErrorDomain) : Result<Nothing>()
    data class Exception(val error: Throwable) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[error=$error]"
            is Exception -> "Exception[exception=$error]"
        }
    }

    val isSuccess: Boolean get() = this is Success

    val isError: Boolean get() = this is Error

    val isException: Boolean get() = this is Exception

    val getOrNull: R? get() = if (isSuccess) (this as Success<R>).data else null
}