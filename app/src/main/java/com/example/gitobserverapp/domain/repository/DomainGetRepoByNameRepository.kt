package com.example.gitobserverapp.domain.repository

import com.example.gitobserverapp.domain.model.DomainReposListModel
import dagger.Provides

interface DomainGetRepoByNameRepository {
    suspend fun getData(searchWord: String, page: Int): DomainReposListModel
    suspend fun saveData(domainReposListModel: DomainReposListModel)
}