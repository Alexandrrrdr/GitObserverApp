package com.example.gitobserverapp.data.network

import com.example.gitobserverapp.data.network.model.repo.GitHubRepoResult
import com.example.gitobserverapp.data.network.model.starred.StarredModelItem
import com.example.gitobserverapp.utils.Constants.API_GET_REPOS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(API_GET_REPOS)
    suspend fun getRepos(
        @Query("q") q: String,
        @Query("sort") sort: String,
        @Query("per_page") per_page: Int
    ): Response<GitHubRepoResult>

    //TODO check api request!!! Postman

    @Headers("Accept: application/vnd.github.star+json")
    @GET("/repos/{owner_login}/{repo_name}/stargazers")
    suspend fun getStarredData(
        @Path("owner_login") owner_login: String,
        @Path("repo_name") repo_name: String,
        @Query("per_page") per_page: Int
    ): Response<List<StarredModelItem>>
}