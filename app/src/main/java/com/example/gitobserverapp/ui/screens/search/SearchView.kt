package com.example.gitobserverapp.ui.screens.search

import com.example.gitobserverapp.ui.screens.search.model.UiRepo
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndStrategy::class)
interface SearchView: MvpView {
    fun showLoading()
    fun showSuccess(list: List<UiRepo>)
    fun showError(error: String)
    fun showNetworkError()
}