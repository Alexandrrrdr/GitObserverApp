package com.example.gitobserverapp.data.repository.network

import com.example.gitobserverapp.utils.Constants.API_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {
    private val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

    val retrofitInstance: ApiService = retrofit.create(ApiService::class.java)
}