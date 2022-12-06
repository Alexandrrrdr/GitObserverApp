package com.example.gitobserverapp.api

import com.example.gitobserverapp.api.model.GetRepos
import com.example.gitobserverapp.utils.Constants.API_GET_REPOS
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(API_GET_REPOS)
    fun getRepos(@Query("q") q: String,
                 @Query("sort") sort: String,
                 @Query("order") order: String,
                 @Query("per_page") per_page: Int,
                 @Query("page") page: Int): Call<GetRepos>
}