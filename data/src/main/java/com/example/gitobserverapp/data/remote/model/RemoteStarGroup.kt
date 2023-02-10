package com.example.gitobserverapp.data.remote.model

import com.squareup.moshi.Json
import java.util.*

data class RemoteStarGroup(
    @Json(name = "starred_at")
    val date: Date,
    @Json(name = "starred_at")
    val users: RemoteStarUser
)

data class RemoteStarUser(
    @Json(name = "id")
    val id: Int,
    @Json(name = "login")
    val name: String,
    @Json(name = "avatar_url")
    val userAvaUrl: String
)