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

class NetworkStatusHelper(context: Context): LiveData<Boolean>(){

    private val validNetworkConnections : MutableSet<Network> = HashSet<Network>()
    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

    private fun getConnectivityManagerCallback() = object : ConnectivityManager.NetworkCallback(){

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            val networkCapability = connectivityManager.getNetworkCapabilities(network)

//            val hasConnectivity = networkCapability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
//            if (hasConnectivity){
//                CoroutineScope(Dispatchers.IO).launch {
//                    if (InternetAvailability.check()){
//                        withContext(Dispatchers.Main){
//                            validNetworkConnections.add(network)
//                            announceStatus()
//                        }
//                    }
//                }
//            }

            val isConnected = networkCapability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
            val hasCellConnect = networkCapability?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
            val hasWifiConnect = networkCapability?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
            if (isConnected){
                CoroutineScope(Dispatchers.IO).launch {
                    if (NetworkStatusRequest.execute()){
                        withContext(Dispatchers.Main){
//                            validNetworkConnections.add(network)
//                            announceStatus()
                            postValue(true)
                        }
                    } else {
                        postValue(false)
                    }
                }
            } else {
                postValue(false)
            }
//            if (networkCapability!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
//                validNetworkConnections.add(network)
//                announceStatus()
//            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)

            postValue(false)
//            validNetworkConnections.remove(network)
//            announceStatus()
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val isConnected = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            val cell = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            val wifi = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            if (isConnected){
                CoroutineScope(Dispatchers.IO).launch {
                    if (NetworkStatusRequest.execute()){
                        withContext(Dispatchers.Main){
                            postValue(true)
//                            validNetworkConnections.add(network)
                        }
                    } else {
                        postValue(false)
                    }
                }

            } else {
                postValue(false)
            }
//            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
//            {
//                CoroutineScope(Dispatchers.IO).launch {
//                    if (InternetAvailability.check()){
//                        withContext(Dispatchers.Main){
//                            postValue(NetworkStatus.Available)
////                            validNetworkConnections.add(network)
//                        }
//                    }
//                }
//            } else {
//                postValue(NetworkStatus.UnAvailable)
////                validNetworkConnections.remove(network)
//            }
////            announceStatus()
        }
    }

//    fun announceStatus(){
//        if (validNetworkConnections.isNotEmpty()){
//            postValue(NetworkStatus.Available)
//        } else {
//            postValue(NetworkStatus.UnAvailable)
//        }
//    }

    override fun onActive() {
        super.onActive()
        connectivityManagerCallback = getConnectivityManagerCallback()
        val networkRequest = NetworkRequest
            .Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, connectivityManagerCallback)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
    }
}