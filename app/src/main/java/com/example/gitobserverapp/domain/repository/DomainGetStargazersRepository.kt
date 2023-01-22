package com.example.gitobserverapp.domain.repository

import com.example.gitobserverapp.domain.model.DomainStargazersListModel

interface DomainGetStargazersRepository {
    suspend fun getData(owner_login: String,
                        repo_name: String,
                        page: Int): DomainStargazersListModel
    suspend fun saveData(domainStargazersListModel: DomainStargazersListModel)
}