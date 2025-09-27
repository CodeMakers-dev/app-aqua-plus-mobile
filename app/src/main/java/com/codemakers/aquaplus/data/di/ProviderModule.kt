package com.codemakers.aquaplus.data.di

import com.codemakers.aquaplus.data.provider.NetworkProviderImpl
import com.codemakers.aquaplus.domain.provider.NetworkProvider
import org.koin.dsl.module

val providerModule = module {
    single<NetworkProvider> { NetworkProviderImpl(get()) }
}

