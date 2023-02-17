package com.example.gitobserverapp.di

import com.example.gitobserverapp.ui.screens.barchart.ChartFragment
import com.example.gitobserverapp.ui.screens.details.DetailsFragment
import com.example.gitobserverapp.ui.screens.main.SearchFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DomainModul::class, DataModul::class, AppModul::class, NetworkModule::class])
interface AppComponent {
    fun inject(chartFragment: ChartFragment)
    fun inject(mainSearchFragment: SearchFragment)
    fun inject(detailsFragment: DetailsFragment)
}