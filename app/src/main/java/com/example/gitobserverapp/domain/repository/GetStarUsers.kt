package com.example.gitobserverapp.domain.repository

import com.example.gitobserverapp.data.remote.GitResponse
import com.example.gitobserverapp.data.remote.model.RemoteStarGroup

interface GetStarUsers {
    suspend fun getData(repo_name: String, owner_login: String, page_number: Int): GitResponse<List<RemoteStarGroup>>
    suspend fun saveData(starGroupList: List<RemoteStarGroup>)
}