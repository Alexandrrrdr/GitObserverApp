package com.example.gitobserverapp.data.remote.model.tmp//package com.example.gitobserverapp.data.remote.model
//
//import com.example.gitobserverapp.domain.model.Repo
//import com.example.gitobserverapp.domain.model.RepoUser
//import com.squareup.moshi.Json
//
//class RemoteRepo(
//    @Json(name = "id")
//    override val id: Int,
//    @Json(name = "name")
//    override val name: String,
//    @Json(name = "owner")
//    override val owner: RemoteRepoUser,
//    @Json(name = "stargazers_count")
//    override val starsCount: Int
//): Repo
//
//class RemoteRepoUser(
//    @Json(name = "id")
//    override val id: Int,
//    @Json(name = "login")
//    override val login: String,
//    @Json(name = "avatar_url")
//    override val avatarUrl: String
//): RepoUser