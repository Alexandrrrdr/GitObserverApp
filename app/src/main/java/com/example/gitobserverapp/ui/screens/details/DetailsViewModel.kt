package com.example.gitobserverapp.ui.screens.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitobserverapp.data.remote.model.RemoteStarGroup

class DetailsViewModel: ViewModel() {
    val usersList: MutableLiveData<List<com.example.gitobserverapp.data.remote.model.RemoteStarGroup>> by lazy {
        MutableLiveData<List<com.example.gitobserverapp.data.remote.model.RemoteStarGroup>>()
    }

    fun setUserList(usersListDetails: List<com.example.gitobserverapp.data.remote.model.RemoteStarGroup>){
        usersList.postValue(usersListDetails)
    }
}