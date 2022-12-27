package com.example.gitobserverapp.utils

import android.content.Context
import com.example.gitobserverapp.App
import com.example.gitobserverapp.di.AppComponent

object Extensions {

    fun Context.appComponent(): AppComponent{
        return when(this){
            is App -> appComponent
            else -> this.applicationContext.appComponent()
        }
    }
}