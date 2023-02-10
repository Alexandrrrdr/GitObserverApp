package com.example.gitobserverapp.domain.usecase.base

interface BaseUseCaseRepoParams<X, Y, Z> {
    suspend fun getData(repo_name: X, page_number: Y): Z
}