package com.example.gitobserverapp.di

import com.example.gitobserverapp.data.remote.GitRetrofitService
import com.example.gitobserverapp.data.repository.ReposImplList
import com.example.gitobserverapp.data.repository.StarsImpl
import com.example.gitobserverapp.data.utils.mapper.RepoToDomain
import com.example.gitobserverapp.data.utils.mapper.StarGroupToDomain
import com.example.gitobserverapp.domain.repository.GetRepos
import com.example.gitobserverapp.domain.repository.GetStars
import com.example.gitobserverapp.utils.mapper.UiRepoMapper
import com.example.gitobserverapp.utils.mapper.UiStarGroupMapper
import dagger.Module
import dagger.Provides

@Module
class DataModul {

    @Provides
    fun provideDomainMapper() = RepoToDomain()

    @Provides
    fun provideUiMapper() = UiRepoMapper()

    @Provides
    fun provideUiStarGroupMapper() = UiStarGroupMapper()

    @Provides
    fun provideStarGroupMapper() = StarGroupToDomain()

    @Provides
    fun provideDomainReposRepository(gitRetrofitService: GitRetrofitService, repoToDomain: RepoToDomain): GetRepos {
        return ReposImplList(gitRetrofitService = gitRetrofitService, repoToDomain = repoToDomain)
    }

    @Provides
    fun provideDomainStargazersRepository(gitRetrofitService: GitRetrofitService, starGroupToDomain: StarGroupToDomain): GetStars {
        return StarsImpl(gitRetrofitService = gitRetrofitService, starGroupToDomain = starGroupToDomain)
    }
}