package com.codemakers.aquaplus.domain.common

sealed class CommonError : ErrorDomain()

object ConnectError : CommonError()