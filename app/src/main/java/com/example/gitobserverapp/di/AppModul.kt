package com.example.gitobserverapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModul(private val context: Context) {

    @Provides
    @Singleton
    fun getContext(): Context{
        return context
    }
}