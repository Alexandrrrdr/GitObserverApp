package com.example.gitobserverapp.ui.screens.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitobserverapp.data.remote.model.RemoteStarGroup

class DetailsViewModel: ViewModel() {
    val usersList: MutableLiveData<List<RemoteStarGroup>> by lazy {
        MutableLiveData<List<RemoteStarGroup>>()
    }

}