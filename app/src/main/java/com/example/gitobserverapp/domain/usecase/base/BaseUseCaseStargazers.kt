package com.example.gitobserverapp.domain.usecase.base

interface BaseUseCaseStargazers<X, Y, T, Z> {
    suspend fun getData(repo_name: X, owner_login: Y, page_number: T) : Z
}