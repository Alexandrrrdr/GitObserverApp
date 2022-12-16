package com.example.gitobserverapp.data.network

import com.example.gitobserverapp.utils.Constants.API_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(logger())
        .baseUrl(API_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val retrofitInstance: ApiService = retrofit.create(ApiService::class.java)

    private fun logger(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
}