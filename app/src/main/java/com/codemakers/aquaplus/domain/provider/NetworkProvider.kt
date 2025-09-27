package com.codemakers.aquaplus.domain.provider

interface NetworkProvider {

    fun isAvailable(): Boolean
}