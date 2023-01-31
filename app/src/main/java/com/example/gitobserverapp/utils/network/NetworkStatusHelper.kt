package com.example.gitobserverapp.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NetworkStatusHelper(context: Context) : LiveData<Boolean>() {

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

    private fun getConnectivityManagerCallback() = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            val networkCapability = connectivityManager.getNetworkCapabilities(network)
//            val isConnected =
//                networkCapability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//                    ?: false

            val isWifeConnected =
                networkCapability?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    ?: false

            val isCellConnected =
                networkCapability?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    ?: false

            if (isWifeConnected || isCellConnected) {
                postValue(true)
            } else {
                postValue(false)
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(false)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
//            val isConnected =
//                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

            val isWifeConnected =
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)

            val isCellConnected =
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)

            if (isWifeConnected || isCellConnected) {
                postValue(true)
            } else {
                postValue(false)
            }
        }
    }

    override fun onActive() {
        super.onActive()
        connectivityManagerCallback = getConnectivityManagerCallback()
        val networkRequest = NetworkRequest
            .Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, connectivityManagerCallback)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
    }
}