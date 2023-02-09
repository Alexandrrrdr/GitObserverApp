package com.example.gitobserverapp.data.remote

import com.example.gitobserverapp.data.remote.model.RemoteRepoResult
import com.example.gitobserverapp.data.remote.model.RemoteStarGroup
import com.example.gitobserverapp.utils.Constants.API_GET_REPOS
import retrofit2.Response
import retrofit2.http.*

interface GitRetrofitService {
    @Headers("Accept: application/vnd.github+json")
    @GET(API_GET_REPOS)
    suspend fun getRepos(
        @Query("q") q: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): Response<RemoteRepoResult>



    @Headers("Accept: application/vnd.github.star+json")
    @GET("/repos/{owner_login}/{repo_name}/stargazers")
    suspend fun getStarredData(
        @Path("repo_name") repo_name: String,
        @Path("owner_login") owner_login: String,
        @Query("per_page") per_page: Int,
        @Query("page") page : Int
    ): Response<List<RemoteStarGroup>>
}