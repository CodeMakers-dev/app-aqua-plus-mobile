package com.codemakers.aquaplus.di

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.codemakers.aquaplus.BuildConfig
import com.codemakers.aquaplus.data.datasource.local.dao.AuthSessionDao
import com.codemakers.aquaplus.data.datasource.remote.AuthApi
import com.codemakers.aquaplus.data.di.dataModule
import com.codemakers.aquaplus.data.di.getChuckerInterceptor
import com.codemakers.aquaplus.data.di.provideHttpClient
import com.codemakers.aquaplus.data.models.response.ProfileDto
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
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

val appModule = module {
    single { Gson() }
    single(qualifier = qualifier("errorInterceptor")) {
        provideErrorInterceptor(
            get(),
            get(),
            get()
        )
    }
    includes(dataModule)
    includes(domainModule)
    includes(uiModule)
    includes(syncModule)
}

fun provideErrorInterceptor(
    context: Context,
    preferencesRepository: PreferencesRepository,
    authSessionDao: AuthSessionDao,
) = Interceptor { chain ->
    val request = chain.request()
    val response = chain.proceed(request)
    if (response.code != HttpURLConnection.HTTP_UNAUTHORIZED) return@Interceptor response

    response.close()

    val profile = preferencesRepository.getObject(USER, ProfileDto::class.java)
    val personId = profile?.person?.id

    val token = getNewToken(context, preferencesRepository, authSessionDao, personId)
    if (token.isNullOrBlank()) {
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

    val newRequest = request
        .newBuilder()
        .header("Authorization", token)
        .build()
    return@Interceptor chain.proceed(newRequest)
}

private fun getNewToken(
    context: Context,
    preferencesRepository: PreferencesRepository,
    authSessionDao: AuthSessionDao,
    personId: Int?,
): String? {
    val maxAttempts = 3
    var attempts = 0

    while (attempts < maxAttempts) {
        attempts++
        try {
            val okHttpClient = provideHttpClient(
                null,
                null,
                getChuckerInterceptor(context),
            )
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val authApi = retrofit.create(AuthApi::class.java)
            val newToken = runBlocking {
                authApi.refreshToken(
                    refreshToken = preferencesRepository.getString(REFRESH_TOKEN).orEmpty()
                ).response.token
            }
            preferencesRepository.setString(TOKEN, newToken)
            if (personId != null) {
                runBlocking {
                    authSessionDao.updateToken(personId, newToken)
                }
            }

            return newToken
        } catch (e: Exception) {
            e.printStackTrace()
            if (attempts >= maxAttempts) {
                return null
            }
        }
    }
    return null
}