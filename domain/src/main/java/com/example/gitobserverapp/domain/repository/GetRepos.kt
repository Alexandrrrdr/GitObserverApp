package com.example.gitobserverapp.domain.repository

import com.example.gitobserverapp.domain.model.Repo
import com.example.gitobserverapp.domain.model.NetworkState


interface GetRepos {
    suspend fun getData(searchWord: String, page: Int): NetworkState<List<Repo>>
}