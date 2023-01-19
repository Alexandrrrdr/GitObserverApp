package com.example.gitobserverapp.domain.repository

import com.example.gitobserverapp.domain.model.DomainReposListModel

interface DomainGetDataByRepoNameRepository {
    suspend fun getData(): DomainReposListModel
    suspend fun saveData(domainReposListModel: DomainReposListModel)
}