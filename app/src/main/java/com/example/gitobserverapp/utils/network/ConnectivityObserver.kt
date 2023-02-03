package com.example.gitobserverapp.utils.network

interface ConnectivityObserver {

    fun checkNetwork(): Boolean
}