package com.example.gitobserverapp.di

import com.example.gitobserverapp.data.network.GitRetrofitService
import com.example.gitobserverapp.utils.Constants
import com.example.gitobserverapp.utils.Constants.END_YOKEN
import com.example.gitobserverapp.utils.Constants.GIT_YOKEN
import com.example.gitobserverapp.utils.Constants.START_YOKEN
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter())
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }


    @Provides
    @Singleton
    fun provideMoshiConverter(moshi: Moshi): MoshiConverterFactory{
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    @Singleton
    fun provideLogger(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain: Interceptor.Chain -> val request = chain
                .request()
                .newBuilder()
                .addHeader("Authorization", GIT_YOKEN + START_YOKEN + END_YOKEN).build()
                return@addInterceptor chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitService(okhttp: OkHttpClient, moshiConverterFactory: MoshiConverterFactory): GitRetrofitService {
        val retrofit: Retrofit = Retrofit.Builder()
            .client(okhttp)
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(moshiConverterFactory)
            .build()
        return retrofit.create(GitRetrofitService::class.java)
    }
}