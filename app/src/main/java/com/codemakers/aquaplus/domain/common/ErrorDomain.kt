package com.codemakers.aquaplus.domain.common

abstract class ErrorDomain {
    open val message: String? = null
    open val code: Int? = null
}