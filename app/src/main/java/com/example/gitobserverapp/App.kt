package com.example.gitobserverapp

import android.app.Application
import com.example.gitobserverapp.di.AppComponent
import com.example.gitobserverapp.di.DaggerAppComponent

class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}