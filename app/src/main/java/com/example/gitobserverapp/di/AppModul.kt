package com.example.gitobserverapp.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
abstract class AppModul(private val context: Context) {

    @Provides
    fun getContext(): Context{
        return context
    }
}