package com.example.gitobserverapp.domain.repository

import com.example.gitobserverapp.domain.model.DomainStargazersListModel

interface GetStarUsers {
    suspend fun getData(repo_name: String, owner_login: String, page_number: Int): DomainStargazersListModel
    suspend fun saveData(domainStargazersListModel: DomainStargazersListModel)
}