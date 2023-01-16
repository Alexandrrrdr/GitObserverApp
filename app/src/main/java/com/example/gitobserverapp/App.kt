package com.example.gitobserverapp

import android.app.Application
import com.example.gitobserverapp.di.AppComponent
import com.example.gitobserverapp.di.DaggerAppComponent

class App: Application() {

    //Доделать функционал, перевести все на Pattern repository, все функции переименовать в repo, модели также переименовать в более общие,
    //Изучить и прикрутить MVP с Moxy к проекту.

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}