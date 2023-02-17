package com.example.gitobserverapp.domain.repository

import com.example.gitobserverapp.domain.model.NetworkState
import com.example.gitobserverapp.domain.model.Repo
import com.example.gitobserverapp.domain.model.StarDate
import com.example.gitobserverapp.domain.model.StarUser


interface GetRepository {
    suspend fun getRepos(userName: String): NetworkState<List<Repo>>
    suspend fun getStarGroup(repoName: String, ownerName: String, pageNumber: Int): NetworkState<List<StarDate>>
}