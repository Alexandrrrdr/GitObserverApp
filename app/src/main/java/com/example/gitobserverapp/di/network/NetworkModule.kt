package com.example.gitobserverapp.di.network

import com.example.gitobserverapp.data.network.ApiService
import com.example.gitobserverapp.utils.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class NetworkModule {


    @Provides
    fun provideRetrofitService(okhttp: OkHttpClient): ApiService {
        val retrofit: Retrofit = Retrofit.Builder()
            .client(okhttp)
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }


    @Provides
    private fun logger(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
}