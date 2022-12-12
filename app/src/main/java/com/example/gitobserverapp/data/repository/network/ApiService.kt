package com.example.gitobserverapp.data.repository.network

import com.example.gitobserverapp.data.repository.network.model.GitHubRepoResult
import com.example.gitobserverapp.utils.Constants.API_GET_REPOS
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(API_GET_REPOS)
    fun getRepos(
        @Query("q") q: String,
        @Query("sort") sort: String): Call<GitHubRepoResult>
}