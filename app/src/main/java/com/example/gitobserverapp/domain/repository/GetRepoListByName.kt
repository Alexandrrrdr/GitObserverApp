package com.example.gitobserverapp.domain.repository

import com.example.gitobserverapp.data.remote.model.NetworkResponse
import com.example.gitobserverapp.domain.model.DomainReposListModel
import com.example.gitobserverapp.ui.screens.main.MainRepo

interface GetRepoListByName {
    suspend fun getData(searchWord: String, page: Int): NetworkResponse<List<MainRepo>>
    suspend fun saveData(domainReposListModel: DomainReposListModel)
}