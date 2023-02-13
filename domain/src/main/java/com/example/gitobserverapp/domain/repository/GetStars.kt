package com.example.gitobserverapp.domain.repository

import com.example.gitobserverapp.domain.model.StarGroup
import com.example.gitobserverapp.domain.model.NetworkState


interface GetStars {
    suspend fun getData(repo_name: String, owner_login: String, page_number: Int): NetworkState<List<StarGroup>>
}