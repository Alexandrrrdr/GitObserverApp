package com.example.gitobserverapp.data.remote.model

import com.example.gitobserverapp.domain.model.Repo
import com.squareup.moshi.Json
import java.util.*

class RemoteRepo(
    override val id: Int,
    override val name: String,
    override val owner: RemoteUser,
    @Json(name = "stargazers_count")
    override val starsCount: Int
): Repo