package com.example.gitobserverapp.di

import com.example.gitobserverapp.data.network.ApiService
import com.example.gitobserverapp.utils.Constants
import com.example.gitobserverapp.utils.Constants.GIT_TOKEN
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.internal.addHeaderLenient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideMoshiConverter(): MoshiConverterFactory{
        return MoshiConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideLogger(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain: Interceptor.Chain -> val request = chain.request().newBuilder().addHeader("Authorization", GIT_TOKEN).build()
                return@addInterceptor chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitService(okhttp: OkHttpClient, moshiConverterFactory: MoshiConverterFactory): ApiService {
        val retrofit: Retrofit = Retrofit.Builder()
            .client(okhttp)
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(moshiConverterFactory)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}