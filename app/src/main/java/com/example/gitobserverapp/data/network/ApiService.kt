package com.example.gitobserverapp.data.network

import androidx.annotation.IntRange
import com.example.gitobserverapp.data.network.model.repo.GitHubRepoResult
import com.example.gitobserverapp.data.network.model.starred.StarredModelItem
import com.example.gitobserverapp.utils.Constants.API_GET_REPOS
import com.example.gitobserverapp.utils.Constants.DEF_PER_PAGE
import com.example.gitobserverapp.utils.Constants.MAX_PER_PAGE
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET(API_GET_REPOS)
    suspend fun getRepos(
        @Query("q") q: String,
        @Query("sort") sort: String,
        @Query("page") @IntRange(from = 1) page: Int = 1,
        @Query("per_page") @IntRange(from = 1L, to = MAX_PER_PAGE.toLong()) per_page: Int = DEF_PER_PAGE
    ): Response<GitHubRepoResult>



    @Headers("Accept: application/vnd.github.star+json")
    @GET("/repos/{owner_login}/{repo_name}/stargazers")
    suspend fun getStarredData(
        @Path("owner_login") owner_login: String,
        @Path("repo_name") repo_name: String,
        @Query("per_page") per_page: Int,
        @Query("page") page : Int
    ): Response<List<StarredModelItem>>
}