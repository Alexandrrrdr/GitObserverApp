package com.example.gitobserverapp.domain.repository

import com.example.gitobserverapp.data.remote.GitResponse
import com.example.gitobserverapp.data.remote.model.RemoteRepoResult

interface GetRepoListByName {
    suspend fun getData(searchWord: String, page: Int): GitResponse<RemoteRepoResult>
    suspend fun saveData(gitResult: GitResponse<RemoteRepoResult>)
}