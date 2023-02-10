package com.example.gitobserverapp.data.remote.model

import com.squareup.moshi.Json

data class RemoteRepoResultList(
    @Json(name = "items")
    val repoList: List<RemoteRepo>
)

data class RemoteRepo(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "stargazers_count")
    val starsCount: Int,
    @Json(name = "owner")
    val owner: RemoteRepoUser
)

data class RemoteRepoUser(
    @Json(name = "id")
    val id: Int,
    @Json(name = "login")
    val login: String,
    @Json(name = "avatar_url")
    val avatarUrl: String
)