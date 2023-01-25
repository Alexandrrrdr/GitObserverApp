package com.example.gitobserverapp.domain.model

import java.time.LocalDate

data class DomainStargazersListModel(
    val stargazers_list: ArrayList<DomainStargazersListItem>
)

data class DomainStargazersListItem(
    val starred_at: LocalDate,
    val id: Int,
    val login: String,
    val avatar_url: String
)

