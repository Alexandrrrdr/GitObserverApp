package com.example.gitobserverapp.data.repository

import com.example.gitobserverapp.data.network.ApiService
import com.example.gitobserverapp.utils.Constants
import com.example.gitobserverapp.utils.Constants.SORT_BY

class ApiRepository(var apiService: ApiService) {

    suspend fun getRepositories(searchName: String) =
        apiService.getRepos(q = searchName, sort = SORT_BY, per_page = Constants.PER_PAGE)

    suspend fun getStarredData(login: String, repoName: String) = apiService.getStarredData(
        owner_login = login,
        repo_name = repoName,
        per_page = Constants.PER_PAGE
    )
}