package com.example.gitobserverapp.ui.screens.details

import com.example.gitobserverapp.ui.screens.barchart.PresentationStargazersListItem
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndStrategy::class)
interface DetailsView : MvpView {
    fun showList(list: List<PresentationStargazersListItem>, period: Int, amountUsers: Int)
}