package com.example.gitobserverapp.domain.model

data class DomainStargazersListModel(
    val stargazersList: ArrayList<DomainStargazersListModelInfo>
)

data class DomainStargazersListModelInfo(
    val starred_at: String,
    val user: DomainStargazersListModelUser
)

data class DomainStargazersListModelUser(
    val id: Int,
    val login: String,
    val avatar_url: String
)

