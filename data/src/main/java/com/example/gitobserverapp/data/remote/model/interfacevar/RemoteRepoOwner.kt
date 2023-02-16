package com.example.gitobserverapp.data.remote.model.interfacevar

import com.example.gitobserverapp.domain.model.RepoOwner
import com.squareup.moshi.Json

class RemoteRepoOwner(
    @Json(name = "avatar_url")
    override val avatarUrl: String = "",
    @Json(name = "id")
    override val id: Int = 0,
    @Json(name = "login")
    override val login: String = ""
): RepoOwner