package com.example.gitobserverapp.presentation.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitobserverapp.presentation.details.model.UserData

class DetailsViewModel: ViewModel() {
    val usersList: MutableLiveData<UserData> by lazy {
        MutableLiveData<UserData>()
    }

    fun setUserList(userData: UserData){
        usersList.postValue(userData)
    }
}