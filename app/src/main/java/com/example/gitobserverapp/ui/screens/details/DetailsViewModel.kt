package com.example.gitobserverapp.ui.screens.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitobserverapp.ui.screens.barchart.PresentationStargazersListItem

class DetailsViewModel: ViewModel() {
    val usersList: MutableLiveData<List<PresentationStargazersListItem>> by lazy {
        MutableLiveData<List<PresentationStargazersListItem>>()
    }

    fun setUserList(usersListDetails: List<PresentationStargazersListItem>){
        usersList.postValue(usersListDetails)
    }
}