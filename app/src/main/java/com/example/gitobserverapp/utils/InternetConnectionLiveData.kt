package com.example.gitobserverapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.LiveData

class InternetConnectionLiveData(private val context: Context): LiveData<Boolean>() {

    private val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var networkConnectionCallback: ConnectivityManager.NetworkCallback

    override fun onActive() {
        super.onActive()
        updateNetworkConnection()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback())
            }
            else -> {
                context.registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.EXTRA_NO_CONNECTIVITY))
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(connectivityManagerCallback())
    }

    private fun connectivityManagerCallback(): ConnectivityManager.NetworkCallback{
        networkConnectionCallback = object : ConnectivityManager.NetworkCallback(){
            override fun onLost(network: Network) {
                super.onLost(network)
                postValue(false)
            }
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                postValue(true)
            }
        }
        return networkConnectionCallback
    }

    private fun updateNetworkConnection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork
            val connection = connectivityManager.getNetworkCapabilities(networkCapabilities)
            if (connection!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                postValue(true)
            } else postValue(false)
        }
    }

    private val networkReceiver = object: BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            updateNetworkConnection()
        }
    }
}