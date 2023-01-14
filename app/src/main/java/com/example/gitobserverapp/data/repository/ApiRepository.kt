package com.example.gitobserverapp.data.repository

import com.example.gitobserverapp.data.network.ApiService
import com.example.gitobserverapp.utils.Constants
import com.example.gitobserverapp.utils.Constants.DEF_PER_PAGE
import com.example.gitobserverapp.utils.Constants.MAX_PER_PAGE
import com.example.gitobserverapp.utils.Constants.SORT_BY
import javax.inject.Inject

class ApiRepository @Inject constructor(var apiService: ApiService) {

    suspend fun getRepositories(searchName: String, page: Int) =
        apiService.getRepos(q = searchName, sort = SORT_BY, page = page, per_page = MAX_PER_PAGE)

    suspend fun getStarredData(login: String, repoName: String, page: Int) = apiService.getStarredData(
        owner_login = login,
        repo_name = repoName,
        per_page = MAX_PER_PAGE,
        page = page
    )
}