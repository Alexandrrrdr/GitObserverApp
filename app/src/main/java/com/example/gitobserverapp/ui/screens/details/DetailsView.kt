package com.example.gitobserverapp.ui.screens.details

import com.example.gitobserverapp.ui.screens.details.model.DetailsUser
import moxy.MvpView
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = SingleStateStrategy::class)
interface DetailsView : MvpView {
    fun showList(list: List<DetailsUser>, period: String, amountUsers: Int)
}