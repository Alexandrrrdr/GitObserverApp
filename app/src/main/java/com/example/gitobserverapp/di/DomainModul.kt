package com.example.gitobserverapp.di

import dagger.Module
import dagger.Provides

@Module
class DomainModul {

    @Provides
    fun provideGetReposUseCase(getRepos: com.example.gitobserverapp.domain.repository.GetRepos): com.example.gitobserverapp.domain.usecase.GetRepoUseCase {
        return com.example.gitobserverapp.domain.usecase.GetRepoUseCase(getRepos = getRepos)
    }

    @Provides
    fun provideGetStargazersUseCase(getStars: com.example.gitobserverapp.domain.repository.GetStars): com.example.gitobserverapp.domain.usecase.GetStarGroupUseCase {
        return com.example.gitobserverapp.domain.usecase.GetStarGroupUseCase(
            getStargazersRepository = getStars
        )
    }
}