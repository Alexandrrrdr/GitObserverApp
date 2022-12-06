package com.example.gitobserverapp.api.model

data class GetRepos(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)