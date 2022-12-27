package com.example.gitobserverapp.di

import com.example.gitobserverapp.data.repository.ApiRepository
import com.example.gitobserverapp.presentation.main.MainFragment
import dagger.Component

@Component(modules = [DaggerComponent::class])
interface AppComponent {
    fun injectApiRepository(): ApiRepository
    fun inject(mainFragment: MainFragment)
}