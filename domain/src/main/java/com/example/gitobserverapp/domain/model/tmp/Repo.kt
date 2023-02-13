package com.example.gitobserverapp.domain.model.tmp

interface Repo {
    val id: Int
    val name: String
    val starsCount: Int
    val owner: RepoUser
}