package com.example.gitobserverapp.ui.screens.main

import com.example.gitobserverapp.data.remote.model.RemoteRepo
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndStrategy::class)
interface MainSearchView: MvpView {
    fun showLoading()
    fun showSuccess(list: List<com.example.gitobserverapp.data.remote.model.RemoteRepo>)
    fun showError(error: String)
    fun showNetworkError()
}