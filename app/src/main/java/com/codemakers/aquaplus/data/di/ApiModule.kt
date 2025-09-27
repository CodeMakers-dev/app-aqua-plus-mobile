package com.codemakers.aquaplus.data.di

import com.codemakers.aquaplus.data.datasource.remote.EmployeeRouteApi
import com.codemakers.aquaplus.data.datasource.remote.InvoiceApi
import com.codemakers.aquaplus.data.datasource.remote.LoginApi
import com.codemakers.aquaplus.data.datasource.remote.UserApi
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    single { get<Retrofit>().create(LoginApi::class.java) }
    single { get<Retrofit>().create(UserApi::class.java) }
    single { get<Retrofit>().create(EmployeeRouteApi::class.java) }
    single { get<Retrofit>().create(InvoiceApi::class.java) }
}

