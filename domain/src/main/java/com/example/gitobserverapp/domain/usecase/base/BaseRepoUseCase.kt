package com.example.gitobserverapp.domain.usecase.base

interface BaseRepoUseCase<X, Y, Z> {
    suspend fun getRepos(repoName: X, pageNumber: Y): Z
}