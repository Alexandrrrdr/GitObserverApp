package com.example.gitobserverapp.domain.usecase.base

interface BaseRepoUseCase<X, Z> {
    suspend fun getRepos(repoName: X): Z
}