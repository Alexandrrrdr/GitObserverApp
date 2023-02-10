package com.example.gitobserverapp.domain.model


data class RepoResultList (
    val repoList: List<Repo>
)

data class Repo (
    val id: Int,
    val name: String,
    val starsCount: Int,
    val owner: RepoUser
)
data class RepoUser (
    val id: Int,
    val login: String,
    val avatarUrl: String
)
