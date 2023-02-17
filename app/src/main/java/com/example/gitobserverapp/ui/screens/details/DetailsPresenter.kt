package com.example.gitobserverapp.ui.screens.details

import com.example.gitobserverapp.ui.screens.details.model.DetailsUser
import moxy.MvpPresenter

class DetailsPresenter: MvpPresenter<DetailsView>() {

    fun showData(arrayList: Array<DetailsUser>, period: String, amountUsers: Int){
        val list = arrayList.toList()
        viewState.showList(list = list, period = period, amountUsers = amountUsers)
    }
}