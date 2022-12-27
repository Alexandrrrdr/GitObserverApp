package com.example.gitobserverapp.di

import com.example.gitobserverapp.data.network.ApiService
import com.example.gitobserverapp.data.network.RetrofitInstance
import com.example.gitobserverapp.data.repository.ApiRepository
import dagger.Module
import dagger.Provides

@Module
class DaggerComponent {

    @Provides
    fun getApiService(): ApiService{
        return RetrofitInstance.retrofitInstance
    }

    @Provides
    fun getApiRepository(): ApiRepository{
        return ApiRepository(apiService = getApiService())
    }
}