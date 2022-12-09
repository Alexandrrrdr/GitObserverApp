package com.example.gitobserverapp.api

import com.example.gitobserverapp.utils.Constants.API_BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance {
    companion object{
        fun getRetrofitClient(): Retrofit{
            return Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }
    }
}