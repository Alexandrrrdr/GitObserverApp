package com.example.gitobserverapp.data.remote.model

import com.example.gitobserverapp.domain.model.StarGroup
import com.squareup.moshi.Json
import java.util.*

class RemoteStarGroup(
    @Json(name = "starred_at")
    override val date: Date,
    @Json(name = "user")
    override val users: RemoteStarUser
): StarGroup