package com.codemakers.aquaplus.ui.extensions

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

val ConnectivityManager.isConnected: Boolean
    get() =
        getNetworkCapabilities(activeNetwork)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
