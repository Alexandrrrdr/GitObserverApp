package com.example.gitobserverapp.presentation.screens.barchart

import java.time.LocalDate

data class PresentationStargazersListModel(
    val stargazers_list: List<PresentationStargazersListItem>
)

data class PresentationStargazersListItem(
    val starred_at: LocalDate,
    val id: Int,
    val login: String,
    val avatar_url: String
)