package com.example.gitobserverapp.domain.repository

import com.example.gitobserverapp.domain.model.StarGroup
import com.example.gitobserverapp.domain.model.NetworkState


interface GetStars {
    suspend fun getData(repoName: String, ownerLogin: String, pageNumber: Int): NetworkState<List<StarGroup>>
}