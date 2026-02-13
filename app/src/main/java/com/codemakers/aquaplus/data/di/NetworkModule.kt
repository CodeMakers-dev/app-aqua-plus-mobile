package com.codemakers.aquaplus.data.di

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.codemakers.aquaplus.BuildConfig
import com.codemakers.aquaplus.data.common.NoAuth
import com.codemakers.aquaplus.data.repository.REFRESH_TOKEN
import com.codemakers.aquaplus.data.repository.TOKEN
import com.codemakers.aquaplus.data.repository.USER
import com.codemakers.aquaplus.domain.repository.PreferencesRepository
import com.codemakers.aquaplus.ui.modules.signin.SignInActivity
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Invocation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit
import kotlin.collections.remove

val networkModule = module {
    single { provideConverterFactory() }
    single(qualifier = qualifier("headerInterceptor")) { getHeaderInterceptor(get()) }
    single(qualifier = qualifier("chuckerInterceptor")) { getChuckerInterceptor(get()) }
    single {
        provideHttpClient(
            get(qualifier = qualifier("headerInterceptor")),
            get(qualifier = qualifier("errorInterceptor")),
            get(qualifier = qualifier("chuckerInterceptor")),
        )
    }
    single(qualifier = qualifier("syncHttpClient")) {
        provideHttpClient(
            null,
            null,
            get(qualifier = qualifier("chuckerInterceptor")),
        )
    }
    single { provideRetrofit(get(), get()) }
    single(qualifier = qualifier("syncRetrofit")) { provideRetrofit(get(qualifier = qualifier("syncHttpClient")), get()) }
}

fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

private fun getHeaderInterceptor(
    preferencesRepository: PreferencesRepository,
): Interceptor = Interceptor { chain ->
    val originalRequest = chain.request()
    val newBuilder = originalRequest.newBuilder()
    val invocation = originalRequest.tag(Invocation::class.java)
    val hasNoAuthAnnotation = invocation?.method()?.isAnnotationPresent(NoAuth::class.java) ?: false
    newBuilder.header("Content-Type", "application/json")

    if (!hasNoAuthAnnotation) {
        val token = preferencesRepository.getString(TOKEN).orEmpty()
        newBuilder.method(chain.request().method, chain.request().body)
        newBuilder.header("Authorization", token)
    }

    chain.proceed(newBuilder.build())
}

fun getChuckerInterceptor(
    context: Context,
): Interceptor = ChuckerInterceptor.Builder(context)
    .collector(
        ChuckerCollector(
            context = context,
            showNotification = true
        )
    )
    .alwaysReadResponseBody(true)
    .build()

fun provideHttpClient(
    headerInterceptor: Interceptor?,
    errorInterceptor: Interceptor?,
    chuckerInterceptor: Interceptor,
): OkHttpClient {
    val client = OkHttpClient
        .Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(chuckerInterceptor)

    if (headerInterceptor != null) {
        client.addInterceptor(headerInterceptor)
    }

    if (errorInterceptor != null) {
        client.addInterceptor(errorInterceptor)
    }

    return client.build()
}

fun provideRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory,
): Retrofit = Retrofit.Builder()
    .baseUrl(BuildConfig.BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(gsonConverterFactory)
    .build()
