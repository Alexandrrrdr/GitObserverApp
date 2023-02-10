package com.example.gitobserverapp.data.remote.model

import com.example.gitobserverapp.domain.model.RepoResultList
import com.squareup.moshi.Json

data class RemoteRepoResultList(
    @Json(name = "items")
    override val repoList: List<RemoteRepo>
): RepoResultList
