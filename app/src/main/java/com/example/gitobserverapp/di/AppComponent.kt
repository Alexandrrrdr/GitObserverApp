package com.example.gitobserverapp.di

import android.app.Application
import com.example.gitobserverapp.presentation.chart.ChartFragment
import com.example.gitobserverapp.presentation.chart.ChartViewModel
import com.example.gitobserverapp.presentation.details.DetailsViewModel
import com.example.gitobserverapp.presentation.main.MainFragment
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [DomainModul::class, DataModul::class, AppModul::class, NetworkModule::class])
interface AppComponent {
    fun inject(mainFragment: MainFragment)
    fun inject(chartFragment: ChartFragment)
    fun inject(chartViewModel: ChartViewModel)
}