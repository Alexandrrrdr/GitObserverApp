package com.example.gitobserverapp.presentation.screens.barchart

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface ChartView: MvpView {
    fun showLoadPage()
    fun showSuccessPage(list: List<BarChartModel>, lastPage: Int, page: Int)
    fun showErrorPage(error: String)
    fun showNetworkErrorPage()
}