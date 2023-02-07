package com.example.gitobserverapp.domain.model

interface Repo {
    val id: Int
    val name: String
    val starsCount: Int
    val owner: User
}