package com.example.gitobserverapp.ui.screens.details

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndStrategy::class)
interface DetailsView : MvpView {
    fun showList(list: List<User>, period: String, amountUsers: Int)
}