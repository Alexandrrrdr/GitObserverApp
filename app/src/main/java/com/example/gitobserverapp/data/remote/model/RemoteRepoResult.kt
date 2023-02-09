package com.example.gitobserverapp.data.remote.model

import com.example.gitobserverapp.domain.model.RepoResult
import com.squareup.moshi.Json

data class RemoteRepoResult(
    @Json(name = "items")
    override val repoList: List<RemoteRepo>
): RepoResult
