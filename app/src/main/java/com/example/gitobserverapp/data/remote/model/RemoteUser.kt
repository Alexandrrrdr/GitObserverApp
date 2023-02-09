package com.example.gitobserverapp.data.remote.model

import com.example.gitobserverapp.domain.model.User
import com.squareup.moshi.Json

class RemoteUser(
    @Json(name = "id")
    override val id: Int,
    @Json(name = "login")
    override val login: String,
    @Json(name = "avatar_url")
    override val avatarUrl: String
): User