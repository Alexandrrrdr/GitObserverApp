package com.example.gitobserverapp.presentation.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitobserverapp.presentation.chart.model.PresentationStargazersListItem

class DetailsViewModel: ViewModel() {
    val usersList: MutableLiveData<List<PresentationStargazersListItem>> by lazy {
        MutableLiveData<List<PresentationStargazersListItem>>()
    }

    fun setUserList(usersListDetails: List<PresentationStargazersListItem>){
        usersList.postValue(usersListDetails)
    }
}