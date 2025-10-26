package com.codemakers.aquaplus.domain.common

sealed class CommonError : ErrorDomain()

data class ConnectError(
    override val message: String? = "Sin conexión a internet"
) : CommonError()