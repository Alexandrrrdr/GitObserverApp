package com.example.gitobserverapp.domain.repository

import com.example.gitobserverapp.domain.model.RepoResultList


interface GetRepos {
    suspend fun getData(searchWord: String, page: Int): RepoResultList
    suspend fun saveData(gitResult: RepoResultList)
}