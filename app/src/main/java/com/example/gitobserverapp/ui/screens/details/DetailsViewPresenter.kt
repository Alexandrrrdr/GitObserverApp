package com.example.gitobserverapp.ui.screens.details

import moxy.MvpPresenter

class DetailsViewPresenter: MvpPresenter<DetailsView>() {

    fun showData(arrayList: Array<User>, period: String, amountUsers: Int){
        val list = arrayList.toList()
        viewState.showList(list = list, period = period, amountUsers = amountUsers)
    }
}