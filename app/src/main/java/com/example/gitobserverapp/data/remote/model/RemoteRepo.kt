package com.example.gitobserverapp.data.remote.model

import com.example.gitobserverapp.domain.model.Repo
import com.squareup.moshi.Json
import java.util.*

class RemoteRepo(
    @Json(name = "created_at")
    override val createdDate: Date,
    override val name: String,
    override val owner: RemoteUser,
    @Json(name = "stargazers_count")
    override val starsCount: Int
): Repo