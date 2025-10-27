package com.codemakers.aquaplus.di

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.codemakers.aquaplus.BuildConfig
import com.codemakers.aquaplus.data.datasource.remote.AuthApi
import com.codemakers.aquaplus.data.di.dataModule
import com.codemakers.aquaplus.data.repository.REFRESH_TOKEN
import com.codemakers.aquaplus.data.repository.TOKEN
import com.codemakers.aquaplus.data.repository.USER
import com.codemakers.aquaplus.domain.di.domainModule
import com.codemakers.aquaplus.domain.repository.PreferencesRepository
import com.codemakers.aquaplus.ui.di.uiModule
import com.codemakers.aquaplus.ui.modules.signin.SignInActivity
import com.codemakers.aquaplus.ui.work.di.syncModule
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

val appModule = module {
    single { Gson() }
    single(qualifier = qualifier("errorInterceptor")) { provideErrorInterceptor(get(), get()) }
    includes(dataModule)
    includes(domainModule)
    includes(uiModule)
    includes(syncModule)
}

fun provideErrorInterceptor(
    context: Context,
    preferencesRepository: PreferencesRepository,
) = Interceptor { chain ->
    val request = chain.request()
    val response = chain.proceed(request)
    if (response.code != HttpURLConnection.HTTP_UNAUTHORIZED) return@Interceptor response

    val token = getNewToken(preferencesRepository)
    if (token == null) {
        preferencesRepository.remove(REFRESH_TOKEN)
        preferencesRepository.remove(TOKEN)
        preferencesRepository.remove(USER)
        context.startActivity(
            Intent(context, SignInActivity::class.java).apply {
                flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_CLEAR_TOP
            }
        )
        return@Interceptor response
    }
    preferencesRepository.setString(TOKEN, token)

    val newRequest = request
        .newBuilder()
        .header("Authorization", "Bearer $token")
        .build()
    return@Interceptor chain.proceed(newRequest)
}

private fun getNewToken(
    preferencesRepository: PreferencesRepository,
) = try {
    val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val authApi = retrofit.create(AuthApi::class.java)
    runBlocking {
        authApi.refreshToken(
            refreshToken = preferencesRepository.getString(REFRESH_TOKEN).orEmpty()
        ).response.token
    }
} catch (e: Exception) {
    e.printStackTrace()
    null
}