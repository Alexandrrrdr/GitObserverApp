package com.example.gitobserverapp.data.remote

import com.example.gitobserverapp.data.remote.model.interfacevar.RemoteRepo
import com.example.gitobserverapp.data.remote.model.interfacevar.RemoteStarUser
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GitRetrofitService {
    @Headers("Accept: application/vnd.github+json")
    @GET("/users/{username}/repos")
    suspend fun getOwnerRepos(
        @Path("username") userName: String,
//        @Query("type") type: String = "owner",
//        @Query("sort") sort: String = "full_name",
//        @Query("direction") direction: String = "asc",
//        @Query("per_page") perPage: Int = 100,    //max is 100
        @Query("page") page: Int
    ): Response<List<RemoteRepo>>


    @Headers("Accept: application/vnd.github.star+json")
    @GET("/repos/{owner_login}/{repo_name}/stargazers")
    suspend fun getStarredData(
        @Path("repo_name") repoName: String,
        @Path("owner_login") ownerLogin: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): Response<List<RemoteStarUser>>
}