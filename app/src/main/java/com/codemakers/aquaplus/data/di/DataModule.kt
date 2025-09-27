package com.codemakers.aquaplus.data.di

import org.koin.dsl.module

val dataModule = module {
    includes(networkModule)
    includes(daoModule)
    includes(apiModule)
    includes(repositoryModule)
    includes(providerModule)
}