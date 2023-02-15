package com.example.gitobserverapp.data.remote.model.interfacevar

import com.example.gitobserverapp.domain.model.interfacesvar.Repo
import com.example.gitobserverapp.domain.model.interfacesvar.RepoUser
import com.squareup.moshi.Json

class RemoteRepo(
    @Json(name = "id")
    override val id: Int,
    @Json(name = "name")
    override val name: String,
    @Json(name = "owner")
    override val owner: RemoteRepoUser,
    @Json(name = "stargazers_count")
    override val starsCount: Int
): Repo

class RemoteRepoUser(
    @Json(name = "id")
    override val id: Int,
    @Json(name = "login")
    override val login: String,
    @Json(name = "avatar_url")
    override val avatarUrl: String
): RepoUser