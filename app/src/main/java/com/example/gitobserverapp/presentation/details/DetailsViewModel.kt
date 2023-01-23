package com.example.gitobserverapp.presentation.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitobserverapp.presentation.chart.model.StargazerModel

class DetailsViewModel: ViewModel() {
    val usersList: MutableLiveData<List<StargazerModel>> by lazy {
        MutableLiveData<List<StargazerModel>>()
    }

    fun setUserList(usersListDetails: List<StargazerModel>){
        usersList.postValue(usersListDetails)
    }
}