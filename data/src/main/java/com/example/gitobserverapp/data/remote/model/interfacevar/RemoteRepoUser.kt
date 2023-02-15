package com.example.gitobserverapp.data.remote.model.interfacevar
import com.example.gitobserverapp.domain.model.interfacesvar.RepoUser
import com.squareup.moshi.Json

class RemoteRepoUser(
    @Json(name = "id")
    override val id: Int,
    @Json(name = "login")
    override val login: String,
    @Json(name = "avatar_url")
    override val avatarUrl: String
): RepoUser