package com.example.gitobserverapp.ui.screens.main

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndStrategy::class)
interface MainSearchView: MvpView {
    fun showLoading()
    fun showSuccess(list: List<RepoItem>)
    fun showError(error: String)
    fun showNetworkError()
}