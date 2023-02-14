package com.example.gitobserverapp.ui.screens.details

import com.example.gitobserverapp.ui.screens.barchart.model.UiStarGroup
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndStrategy::class)
interface DetailsView : MvpView {
    fun showList(list: List<UiStarGroup>, period: Int, amountUsers: Int)
}