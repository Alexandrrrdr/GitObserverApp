package com.example.gitobserverapp.di

import com.example.gitobserverapp.presentation.screens.barchart.ChartFragment
import com.example.gitobserverapp.presentation.screens.barchart.ChartViewModel
import com.example.gitobserverapp.presentation.screens.main.MainSearchFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DomainModul::class, DataModul::class, AppModul::class, NetworkModule::class])
interface AppComponent {
//    fun inject(mainFragment: MainFragment)
    fun inject(chartFragment: ChartFragment)
    fun inject(chartViewModel: ChartViewModel)
    fun inject(mainSearchFragment: MainSearchFragment)
}