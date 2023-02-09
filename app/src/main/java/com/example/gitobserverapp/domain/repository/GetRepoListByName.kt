package com.example.gitobserverapp.domain.repository

import com.example.gitobserverapp.data.remote.model.GitResponse
import com.example.gitobserverapp.data.remote.model.RemoteRepo
import com.example.gitobserverapp.data.remote.model.RemoteResult
import com.example.gitobserverapp.domain.model.DomainReposListModel

interface GetRepoListByName {
    suspend fun getData(searchWord: String, page: Int): GitResponse<RemoteResult>
    suspend fun saveData(gitResult: GitResponse<RemoteResult>)
}