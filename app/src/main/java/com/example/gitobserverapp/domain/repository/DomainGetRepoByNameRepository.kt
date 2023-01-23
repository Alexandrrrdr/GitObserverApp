package com.example.gitobserverapp.domain.repository

import com.example.gitobserverapp.domain.model.DomainReposListModel

interface DomainGetRepoByNameRepository {
    suspend fun getData(searchWord: String, page: Int): DomainReposListModel
    suspend fun saveData(domainReposListModel: DomainReposListModel)
}