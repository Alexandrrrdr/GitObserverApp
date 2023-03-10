package com.example.gitobserverapp.ui.screens.search

import com.example.gitobserverapp.ui.screens.search.model.UiRepo
import moxy.MvpView
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = SingleStateStrategy::class)
interface SearchView: MvpView {
    fun showLoading()
    fun showSuccess(list: List<UiRepo>, isLoading: Boolean, isLoadAvailable: Boolean, currentPage: Int)
    fun showError(error: String, typeError: Int)
    fun showNetworkError()
}