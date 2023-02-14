package com.example.gitobserverapp.ui.screens.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitobserverapp.ui.screens.barchart.model.UiStarGroup

class DetailsViewModel: ViewModel() {
    val usersList: MutableLiveData<List<UiStarGroup>> by lazy {
        MutableLiveData<List<UiStarGroup>>()
    }
    fun setUserList(usersListDetails: List<UiStarGroup>){
        usersList.postValue(usersListDetails)
    }
}