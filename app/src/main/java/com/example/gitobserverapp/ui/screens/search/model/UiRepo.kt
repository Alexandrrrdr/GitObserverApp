package com.example.gitobserverapp.ui.screens.search.model

import com.example.gitobserverapp.domain.model.Repo
import java.util.*

data class UiRepo(
    override val id: Int,
    override val description: String? = null,
    override val name: String,
    override val owner: UiRepoOwner,
    override val created: Date,
    override val starUserAmount: Int
): Repo


