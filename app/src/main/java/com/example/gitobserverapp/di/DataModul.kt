package com.example.gitobserverapp.di

import com.example.gitobserverapp.data.network.ApiService
import com.example.gitobserverapp.data.repository.StarRepository
import dagger.Module
import dagger.Provides

@Module
class DataModul {

    @Provides
    fun provideApiRepository(apiService: ApiService): StarRepository{
        return StarRepository(apiService)
    }
}