package com.example.gitobserverapp.di

import com.example.gitobserverapp.presentation.chart.ChartFragment
import com.example.gitobserverapp.presentation.chart.ChartViewModel
//import com.example.gitobserverapp.presentation.main.MainFragment
import com.example.gitobserverapp.presentation.main.MainSearchFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DomainModul::class, DataModul::class, AppModul::class, NetworkModule::class])
interface AppComponent {
//    fun inject(mainFragment: MainFragment)
    fun inject(chartFragment: ChartFragment)
//    fun inject(chartViewModel: ChartViewModel)
    fun inject(mainSearchFragment: MainSearchFragment)
}