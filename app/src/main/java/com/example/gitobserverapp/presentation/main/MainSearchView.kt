package com.example.gitobserverapp.presentation.main

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.gitobserverapp.presentation.main.model.RepoItem

@StateStrategyType(value = AddToEndStrategy::class)
interface MainSearchView: MvpView {
    fun showLoading()
    fun showSuccess(list: List<RepoItem>)
    fun showError(error: String)
    fun showNetworkError()
}