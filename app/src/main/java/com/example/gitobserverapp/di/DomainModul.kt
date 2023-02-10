package com.example.gitobserverapp.di

import com.example.gitobserverapp.domain.repository.GetRepos
import com.example.gitobserverapp.domain.repository.GetStars
import com.example.gitobserverapp.domain.usecase.GetRepoUseCase
import com.example.gitobserverapp.domain.usecase.GetStarGroupUseCase
import dagger.Module
import dagger.Provides

@Module
class DomainModul {

    @Provides
    fun provideGetReposUseCase(getRepos: GetRepos): GetRepoUseCase {
        return GetRepoUseCase(getRepos = getRepos)
    }

    @Provides
    fun provideGetStargazersUseCase(getStars: GetStars): GetStarGroupUseCase {
        return GetStarGroupUseCase(getStars = getStars
        )
    }
}