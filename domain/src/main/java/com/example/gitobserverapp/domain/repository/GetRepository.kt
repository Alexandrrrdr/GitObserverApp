package com.example.gitobserverapp.domain.repository

import com.example.gitobserverapp.domain.model.Repo
import com.example.gitobserverapp.domain.model.StarDate


interface GetRepository {
    suspend fun getRepos(userName: String): List<Repo>
    suspend fun getStarGroup(repoName: String, ownerName: String, pageNumber: Int): List<StarDate>
}