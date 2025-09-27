package com.codemakers.aquaplus.data.provider

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import com.codemakers.aquaplus.domain.provider.NetworkProvider

class NetworkProviderImpl(
    private val context: Context,
) : NetworkProvider {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun isAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val isAvailable = connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            }
        return isAvailable == true
    }
}