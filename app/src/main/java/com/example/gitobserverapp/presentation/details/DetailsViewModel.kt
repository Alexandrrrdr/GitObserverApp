package com.example.gitobserverapp.presentation.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitobserverapp.presentation.chart.model.PresentationChartListItem

class DetailsViewModel: ViewModel() {
    val usersList: MutableLiveData<List<PresentationChartListItem>> by lazy {
        MutableLiveData<List<PresentationChartListItem>>()
    }

    fun setUserList(usersListDetails: List<PresentationChartListItem>){
        usersList.postValue(usersListDetails)
    }
}