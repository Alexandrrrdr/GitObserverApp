package com.example.gitobserverapp.utils.network

import android.content.Context


interface ConnectivityObserver {

    fun checkNetwork(): Boolean
}