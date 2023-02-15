package com.example.gitobserverapp.data.remote

import com.example.gitobserverapp.data.remote.model.RemoteRepoResultList
import com.example.gitobserverapp.data.remote.model.RemoteStarGroup
import com.example.gitobserverapp.data.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GitRetrofitService {
    @Headers("Accept: application/vnd.github+json")
    @GET("/users/{username}/repos")
    suspend fun getOwnerRepos(
        @Path("username") username: String,
        @Query("per_page") parPage: Int,    //max is 100
        @Query("page") page: Int
    ): Response<RemoteRepoResultList>


    @Headers("Accept: application/vnd.github.star+json")
    @GET("/repos/{owner_login}/{repo_name}/stargazers")
    suspend fun getStarredData(
        @Path("repo_name") repoName: String,
        @Path("owner_login") ownerLogin: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): Response<List<RemoteStarGroup>>
}