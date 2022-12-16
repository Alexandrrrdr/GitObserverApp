package com.example.gitobserverapp.presentation.chart.model

import java.time.LocalDate

data class StarParsedModel(
    val starred_at: LocalDate,
    val repoOwnerName: String
)
