package com.example.gitobserverapp.domain.model

data class DomainReposListModel(
    val items: List<DomainRepoListModelItems>,
)

data class DomainRepoListModelItems(
    val created_at: String,
    val name: String,
    val owner: DomainRepoListModelOwner,
    val stargazers_count: Int
)

data class DomainRepoListModelOwner(
    val avatar_url: String,
    val login: String
)
