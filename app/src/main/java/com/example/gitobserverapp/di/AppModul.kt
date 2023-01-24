package com.example.gitobserverapp.di

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.gitobserverapp.domain.usecase.GetReposUseCase
import com.example.gitobserverapp.domain.usecase.GetStargazersUseCase
import com.example.gitobserverapp.presentation.chart.ChartViewModelFactory
import com.example.gitobserverapp.presentation.main.MainViewModelFactory
import com.example.gitobserverapp.utils.network.NetworkStatusHelper
import dagger.Module
import dagger.Provides

@Module
class AppModul(private val context: Context) {

    @Provides
    fun getContext(): Context{
        return context
    }

    @Provides
    fun provideMainViewModelFactory(getReposUseCase: GetReposUseCase): MainViewModelFactory {
        return MainViewModelFactory(getReposUseCase = getReposUseCase)
    }

    @Provides
    fun provideChartViewModelFactory(getStargazersUseCase: GetStargazersUseCase): ChartViewModelFactory{
        return ChartViewModelFactory(getStargazersUseCase = getStargazersUseCase)
    }

    @Provides
    fun provideNetworkStatusHelper(context: Context): NetworkStatusHelper{
        return NetworkStatusHelper(context = context)
    }
}