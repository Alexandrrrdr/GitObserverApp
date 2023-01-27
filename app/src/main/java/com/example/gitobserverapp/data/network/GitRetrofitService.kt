package com.example.gitobserverapp.data.network

import androidx.annotation.IntRange
import com.example.gitobserverapp.data.network.model.DataReposListModel
import com.example.gitobserverapp.data.network.model.DataStargazersListItem
import com.example.gitobserverapp.utils.Constants
import com.example.gitobserverapp.utils.Constants.API_GET_REPOS
import com.example.gitobserverapp.utils.Constants.DEF_PER_PAGE
import com.example.gitobserverapp.utils.Constants.MAX_PER_PAGE
import retrofit2.Response
import retrofit2.http.*

interface GitRetrofitService {

    @Headers("Accept: application/vnd.github+json")
    @GET(API_GET_REPOS)
    suspend fun getRepos(
        @Query("q") q: String,
        @Query("sort") sort: String,
        @Query("page") @IntRange(from = 1) page: Int = 1,
        @Query("per_page") @IntRange(from = 1L, to = MAX_PER_PAGE.toLong()) per_page: Int = DEF_PER_PAGE
    ): Response<DataReposListModel>



    @Headers("Accept: application/vnd.github.star+json")
    @GET("/repos/{owner_login}/{repo_name}/stargazers")
    suspend fun getStarredData(
        @Path("repo_name") repo_name: String,
        @Path("owner_login") owner_login: String,
        @Query("per_page") per_page: Int,
        @Query("page") page : Int
    ): Response<List<DataStargazersListItem>>
}