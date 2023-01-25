package com.example.gitobserverapp.presentation.chart.model

import java.time.LocalDate

data class PresentationStargazersListModel(
    val stargazers_list: ArrayList<PresentationStargazersListItem>
)

data class PresentationStargazersListItem(
    val starred_at: LocalDate,
    val id: Int,
    val login: String,
    val avatar_url: String
)