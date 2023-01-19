package com.example.gitobserverapp.domain.repository

import com.example.gitobserverapp.domain.model.DomainStargazersListModel

interface DomainGetStargazersRepository {
    suspend fun getData(): DomainStargazersListModel
    suspend fun saveData(domainStargazersListModel: DomainStargazersListModel)
}