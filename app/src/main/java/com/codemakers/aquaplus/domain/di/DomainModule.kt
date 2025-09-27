package com.codemakers.aquaplus.domain.di

import org.koin.dsl.module

val domainModule = module {
    includes(useCaseModule)
}