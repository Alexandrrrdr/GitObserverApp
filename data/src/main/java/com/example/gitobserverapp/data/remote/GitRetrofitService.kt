package com.example.gitobserverapp.data.remote

import com.example.gitobserverapp.data.remote.model.RemoteRepo
import com.example.gitobserverapp.data.remote.model.RemoteStarDate
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
        @Path("username") userName: String,
//        @Query("type") type: String = "owner",
//        @Query("sort") sort: String = "full_name",
//        @Query("direction") direction: String = "asc",
//        @Query("per_page") perPage: Int,    //max is 100
        @Query("page") page: Int
    ): Response<List<RemoteRepo>>


    @Headers("Accept: application/vnd.github.star+json")
    @GET("/repos/{owner_login}/{repo_name}/stargazers")
    suspend fun getStarUsers(
        @Path("repo_name") repoName: String,
        @Path("owner_login") ownerLogin: String,
        @Query("per_page") perPage: Int = Constants.MAX_PER_PAGE,
        @Query("page") page: Int
    ): Response<List<RemoteStarDate>>
}