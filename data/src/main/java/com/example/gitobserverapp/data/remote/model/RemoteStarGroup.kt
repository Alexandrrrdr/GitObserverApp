package com.example.gitobserverapp.data.remote.model

import com.example.gitobserverapp.domain.model.StarGroup
import com.example.gitobserverapp.domain.model.StarUser
import com.squareup.moshi.Json
import java.util.*

data class RemoteStarGroup(
    @Json(name = "starred_at")
    override val date: Date,
    @Json(name = "user")
    override val users: RemoteStarUser
): StarGroup

data class RemoteStarUser(
    @Json(name = "id")
    override val id: Int,
    @Json(name = "login")
    override val name: String,
    @Json(name = "avatar_url")
    override val userAvaUrl: String
): StarUser