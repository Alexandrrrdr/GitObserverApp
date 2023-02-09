package com.example.gitobserverapp.data.remote.model

import com.example.gitobserverapp.domain.model.Repo
import com.squareup.moshi.Json
import java.util.*

class RemoteRepo(
    @Json(name = "id")
    override val id: Int,
    @Json(name = "name")
    override val name: String,
    @Json(name = "owner")
    override val owner: RemoteUser,
    @Json(name = "stargazers_count")
    override val starsCount: Int
): Repo