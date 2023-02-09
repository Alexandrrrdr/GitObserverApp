package com.example.gitobserverapp.domain.repository

import com.example.gitobserverapp.data.remote.GitResponse
import com.example.gitobserverapp.data.remote.model.RemoteRepoResultList

interface GetRepoListByName {
    suspend fun getData(searchWord: String, page: Int): GitResponse<RemoteRepoResultList>
    suspend fun saveData(gitResult: GitResponse<RemoteRepoResultList>)
}