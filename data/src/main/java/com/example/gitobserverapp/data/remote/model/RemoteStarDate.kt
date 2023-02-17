package com.example.gitobserverapp.data.remote.model

import com.example.gitobserverapp.domain.model.StarDate
import com.squareup.moshi.Json
import java.util.*

data class RemoteStarDate (
    @Json(name = "starred_at")
    override val date: Date,
    @Json(name = "user")
    override val user: RemoteStarUser
): StarDate