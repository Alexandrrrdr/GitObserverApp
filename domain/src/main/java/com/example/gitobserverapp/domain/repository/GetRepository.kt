package com.example.gitobserverapp.domain.repository

import com.example.gitobserverapp.domain.model.Repo
import com.example.gitobserverapp.domain.model.StarDateSorted


interface GetRepository {
    suspend fun getRepos(userName: String, pageNumber: Int): List<Repo>
    suspend fun getStarGroup(repoName: String, ownerName: String, lastPage: Int): StarDateSorted
}