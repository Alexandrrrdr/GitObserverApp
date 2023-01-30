package com.example.gitobserverapp.presentation.views

import com.example.gitobserverapp.presentation.main.model.RepoItem
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