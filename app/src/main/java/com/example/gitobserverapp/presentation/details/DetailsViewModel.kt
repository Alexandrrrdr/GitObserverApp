package com.example.gitobserverapp.presentation.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitobserverapp.presentation.details.model.UsersListModel

class DetailsViewModel: ViewModel() {
    val usersList: MutableLiveData<UsersListModel> by lazy {
        MutableLiveData<UsersListModel>()
    }

    fun setUserList(usersListModel: UsersListModel){
        usersList.postValue(usersListModel)
    }
}