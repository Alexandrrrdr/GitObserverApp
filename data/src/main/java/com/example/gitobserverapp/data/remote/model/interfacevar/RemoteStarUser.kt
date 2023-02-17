package com.example.gitobserverapp.data.remote.model.interfacevar

import com.example.gitobserverapp.domain.model.StarUser
import com.squareup.moshi.Json
import java.util.*

data class RemoteStarUser(
    @Json(name = "id")
    override val id: Int,
    @Json(name = "login")
    override val name: String,
    @Json(name = "avatar_url")
    override val userUrl: String
): StarUser
