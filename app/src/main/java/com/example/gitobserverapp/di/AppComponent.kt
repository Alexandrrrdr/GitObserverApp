package com.example.gitobserverapp.di

import com.example.gitobserverapp.presentation.screens.barchart.ChartFragment
import com.example.gitobserverapp.presentation.screens.main.MainSearchFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DomainModul::class, DataModul::class, AppModul::class, NetworkModule::class])
interface AppComponent {
    fun inject(chartFragment: ChartFragment)
    fun inject(mainSearchFragment: MainSearchFragment)
}