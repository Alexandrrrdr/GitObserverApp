package com.example.gitobserverapp.data.network.model

import com.example.gitobserverapp.domain.model.Repo
import com.squareup.moshi.Json
import java.util.*

class DataRepo(
    @Json(name = "created_at")
    override val createdDate: Date,
    override val name: String,
    override val owner: DataUser,
    @Json(name = "stargazers_count")
    override val starsCount: Int
): Repo