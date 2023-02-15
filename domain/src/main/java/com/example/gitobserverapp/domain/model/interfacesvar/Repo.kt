package com.example.gitobserverapp.domain.model.interfacesvar

interface Repo {
    val id: Int
    val name: String
    val owner: RepoUser
}