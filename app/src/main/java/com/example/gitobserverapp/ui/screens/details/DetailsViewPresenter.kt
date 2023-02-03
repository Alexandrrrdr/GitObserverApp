package com.example.gitobserverapp.ui.screens.details

import com.example.gitobserverapp.ui.screens.barchart.PresentationStargazersListItem
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class DetailsViewPresenter: MvpPresenter<DetailsView>() {

    fun showData(list: List<PresentationStargazersListItem>, period: Int, amountUsers: Int){
        viewState.showList(list = list, period = period, amountUsers = amountUsers)
    }
}