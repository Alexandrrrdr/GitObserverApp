package com.example.gitobserverapp.di

import com.example.gitobserverapp.data.network.ApiService
import com.example.gitobserverapp.data.repository.ApiRepository
import dagger.Module
import dagger.Provides

@Module
class DataModul {

    @Provides
    fun provideApiRepository(apiService: ApiService): ApiRepository{
        return ApiRepository(apiService)
    }
}