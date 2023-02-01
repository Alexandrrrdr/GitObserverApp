package com.example.gitobserverapp.presentation.screens.barchart

import com.example.gitobserverapp.presentation.chart.model.BarChartModel
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndStrategy::class)
interface ChartView: MvpView {
    fun showLoadPage()
    fun showSuccessPage(list: List<BarChartModel>)
    fun showErrorPage(error: String)
    fun showNetworkErrorPage()
}