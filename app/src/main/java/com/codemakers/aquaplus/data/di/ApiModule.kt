package com.codemakers.aquaplus.data.di

import com.codemakers.aquaplus.data.datasource.remote.EmployeeRouteApi
import com.codemakers.aquaplus.data.datasource.remote.InvoiceApi
import com.codemakers.aquaplus.data.datasource.remote.AuthApi
import com.codemakers.aquaplus.data.datasource.remote.UserApi
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    single { get<Retrofit>(qualifier = qualifier("syncRetrofit")).create(AuthApi::class.java) }
    single { get<Retrofit>().create(UserApi::class.java) }
    single { get<Retrofit>().create(EmployeeRouteApi::class.java) }
    single { get<Retrofit>(qualifier = qualifier("syncRetrofit")).create(InvoiceApi::class.java) }
}

