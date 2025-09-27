package com.codemakers.aquaplus.ui.work.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.work.WorkManager
import com.codemakers.aquaplus.ui.work.ConnectManager
import com.codemakers.aquaplus.ui.work.SyncDataManager
import com.codemakers.aquaplus.ui.work.SyncDataWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val syncModule = module {
    single { get<Context>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    singleOf(::ConnectManager)
    single { WorkManager.getInstance(get()) }
    singleOf(::SyncDataManager)
    workerOf(::SyncDataWorker)
}