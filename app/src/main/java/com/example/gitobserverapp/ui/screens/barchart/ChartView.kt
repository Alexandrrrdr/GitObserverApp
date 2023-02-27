package com.example.gitobserverapp.ui.screens.barchart

import com.example.gitobserverapp.ui.screens.barchart.model.BarChartModel
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface ChartView: MvpView {
    fun showLoadPage()
    fun showSuccessPage(list: List<BarChartModel>, lastPage: Int, page: Int, isLoadAllowed: Boolean)
    fun showErrorPage(error: String)
    fun showNetworkErrorPage()
}