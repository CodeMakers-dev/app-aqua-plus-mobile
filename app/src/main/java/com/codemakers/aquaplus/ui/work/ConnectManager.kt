package com.codemakers.aquaplus.ui.work

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.codemakers.aquaplus.ui.extensions.isConnected
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ConnectManager(
    connectivityManager: ConnectivityManager,
) {

    val isConnected: Flow<Boolean> by lazy {
        callbackFlow<Boolean> {

            val callback = try {
                object : ConnectivityManager.NetworkCallback() {

                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        trySend(true)
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        trySend(false)
                    }
                }
            } catch (e: Exception) {
                null
            }

            val request = try {
                NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).apply {
                        addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    }
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .build()
            } catch (e: Exception) {
                null
            }

            trySend(connectivityManager.isConnected)

            if (request != null && callback != null) {
                try {
                    connectivityManager.registerNetworkCallback(request, callback)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            awaitClose {
                callback?.let { connectivityManager.unregisterNetworkCallback(it) }
            }
        }
    }
}