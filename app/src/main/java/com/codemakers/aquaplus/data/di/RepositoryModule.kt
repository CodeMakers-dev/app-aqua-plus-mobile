package com.codemakers.aquaplus.data.di

import com.codemakers.aquaplus.data.repository.EmployeeRouteRepositoryImpl
import com.codemakers.aquaplus.data.repository.InvoiceRepositoryImpl
import com.codemakers.aquaplus.data.repository.AuthRepositoryImpl
import com.codemakers.aquaplus.data.repository.PreferencesRepositoryImpl
import com.codemakers.aquaplus.data.repository.UserRepositoryImpl
import com.codemakers.aquaplus.domain.repository.EmployeeRouteRepository
import com.codemakers.aquaplus.domain.repository.InvoiceRepository
import com.codemakers.aquaplus.domain.repository.AuthRepository
import com.codemakers.aquaplus.domain.repository.PreferencesRepository
import com.codemakers.aquaplus.domain.repository.UserRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::PreferencesRepositoryImpl) bind (PreferencesRepository::class)
    singleOf(::AuthRepositoryImpl) bind (AuthRepository::class)
    singleOf(::UserRepositoryImpl) bind (UserRepository::class)
    singleOf(::EmployeeRouteRepositoryImpl) bind (EmployeeRouteRepository::class)
    singleOf(::InvoiceRepositoryImpl) bind (InvoiceRepository::class)
}

