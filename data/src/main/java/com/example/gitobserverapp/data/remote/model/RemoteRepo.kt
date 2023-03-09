package com.example.gitobserverapp.data.remote.model

import com.example.gitobserverapp.domain.model.Repo
import com.squareup.moshi.Json
import java.util.*

data class RemoteRepo(
    @Json(name = "id")
    override val id: Int = 0,
    @Json(name = "description")
    override val description: String? = null,
    @Json(name = "name")
    override val name: String = "",
    @Json(name = "owner")
    override val owner: RemoteRepoOwner,
    @Json(name = "created_at")
    override val created: Date,
    @Json(name = "stargazers_count")
    override val starUserAmount: Int = 0
): Repo