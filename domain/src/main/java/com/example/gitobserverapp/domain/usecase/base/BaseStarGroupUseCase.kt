package com.example.gitobserverapp.domain.usecase.base

interface BaseStarGroupUseCase<X, Y, T, Z> {
    suspend fun getStarGroup(repoName: X, ownerName: Y, lastPage: T) : Z
}