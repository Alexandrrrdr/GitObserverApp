package com.example.gitobserverapp.domain.model

data class DomainStargazersListModel(
    val stargazersList: List<StargazersList>
)

data class StargazersList(
    val starred_at: String,
    val id: Int,
    val login: String,
    val avatar_url: String
)

