package com.example.gitobserverapp.data.repository.network

import com.example.gitobserverapp.data.repository.network.model.repo.GitHubRepoResult
import com.example.gitobserverapp.data.repository.network.model.starred.StarredModel
import com.example.gitobserverapp.data.repository.network.model.starred.StarredModelItem
import com.example.gitobserverapp.utils.Constants.API_GET_REPOS
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(API_GET_REPOS)
    fun getRepos(
        @Query("q") q: String,
        @Query("sort") sort: String): Call<GitHubRepoResult>

    @Headers("Accept: application/vnd.github.star+json")
    @GET("/repos/{owner_login}/{repo_name}/stargazers")
    fun getStarredData(
        @Path("owner_login") owner_login: String,
        @Path("repo_name") repo_name: String) : Call<ArrayList<StarredModelItem>>
}