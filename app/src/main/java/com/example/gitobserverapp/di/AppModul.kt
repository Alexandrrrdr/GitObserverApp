package com.example.gitobserverapp.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module

@Module
abstract class AppModul {
    @Binds
    abstract fun bindContext(application: Application): Context
}