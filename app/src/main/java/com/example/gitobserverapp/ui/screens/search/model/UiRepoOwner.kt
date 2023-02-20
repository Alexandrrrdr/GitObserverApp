package com.example.gitobserverapp.ui.screens.search.model

import com.example.gitobserverapp.domain.model.RepoOwner

data class UiRepoOwner(
    override val avatarUrl: String,
    override val id: Int,
    override val login: String
): RepoOwner
