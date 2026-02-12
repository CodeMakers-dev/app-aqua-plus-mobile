package com.codemakers.aquaplus

import android.app.Application
import com.codemakers.aquaplus.di.appModule
import com.codemakers.aquaplus.ui.work.ConnectManager
import com.codemakers.aquaplus.ui.work.SyncDataManager
import io.realm.kotlin.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class AppApplication : Application(), CoroutineScope by MainScope() {

    private val realm: Realm by inject()

    private val connectManager: ConnectManager by inject()

    private val syncDataManager: SyncDataManager by inject()

    private var isConnected: Boolean = false

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AppApplication)
            androidLogger()
            modules(appModule)
            workManagerFactory()
        }
        validateInternetConnection()
        startSync()
    }

    private fun validateInternetConnection() = launch {
        connectManager.isConnected.collectLatest {
            isConnected = it
        }
    }

    private fun startSync() = launch {
        do {
            if (isConnected) syncDataManager.start()
            delay(60000)
        } while (true)
    }

    override fun onTerminate() {
        super.onTerminate()
        if (!realm.isClosed()) {
            realm.close()
        }
    }
}