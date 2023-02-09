package com.example.gitobserverapp.data.remote.model

import com.example.gitobserverapp.domain.model.Result
import com.squareup.moshi.Json

data class RemoteResult(
    @Json(name = "items")
    override val repoList: List<RemoteRepo>
): Result
