package com.example.gitobserverapp.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities

class NetworkStatusHelper(private val context: Context): ConnectivityObserver {

    override fun checkNetwork(): Boolean {
        val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var hasNetworkConnectivity = false
        object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                val networkCapability = connectivityManager.getNetworkCapabilities(network)
                val isWifeConnected =
                    networkCapability?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        ?: false

                val isCellConnected =
                    networkCapability?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        ?: false

                hasNetworkConnectivity = isWifeConnected || isCellConnected
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                hasNetworkConnectivity = false
            }

            override fun onUnavailable() {
                super.onUnavailable()
                hasNetworkConnectivity = false
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)

                val isWifeConnected =
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)

                val isCellConnected =
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)

                hasNetworkConnectivity = isWifeConnected || isCellConnected
            }
        }
        return hasNetworkConnectivity
    }
}