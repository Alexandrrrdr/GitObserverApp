package com.example.gitobserverapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gitobserverapp.presentation.chart.ChartViewModel
import com.example.gitobserverapp.presentation.details.DetailsViewModel
import com.example.gitobserverapp.presentation.main.MainViewModel
import dagger.Binds
import dagger.MapKey
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun mainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChartViewModel::class)
    abstract fun chartViewModel(viewModel: ChartViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    abstract fun detailsViewModel(viewModel: DetailsViewModel): ViewModel

    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    @MapKey
    internal annotation class ViewModelKey(val value: KClass<out ViewModel>)
}