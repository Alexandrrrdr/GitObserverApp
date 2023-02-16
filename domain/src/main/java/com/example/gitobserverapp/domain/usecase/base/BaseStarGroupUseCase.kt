package com.example.gitobserverapp.domain.usecase.base

interface BaseStarGroupUseCase<X, Y, T, Z> {
    suspend fun getStarGroup(repo_name: X, owner_login: Y, page_number: T) : Z
}