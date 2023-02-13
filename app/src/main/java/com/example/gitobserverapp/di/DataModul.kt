package com.example.gitobserverapp.di

import com.example.gitobserverapp.data.remote.GitRetrofitService
import com.example.gitobserverapp.data.repository.ReposImplList
import com.example.gitobserverapp.data.repository.StarsImpl
import com.example.gitobserverapp.data.utils.mapper.MapToDomain
import com.example.gitobserverapp.domain.repository.GetRepos
import com.example.gitobserverapp.domain.repository.GetStars
import com.example.gitobserverapp.utils.mapper.UiMapper
import dagger.Module
import dagger.Provides

@Module
class DataModul {

    @Provides
    fun provideDomainMapper() = MapToDomain()

    @Provides
    fun provideUiMapper() = UiMapper()

    @Provides
    fun provideDomainReposRepository(gitRetrofitService: GitRetrofitService, mapToDomain: MapToDomain): GetRepos {
        return ReposImplList(gitRetrofitService = gitRetrofitService, mapToDomain = mapToDomain)
    }

    @Provides
    fun provideDomainStargazersRepository(gitRetrofitService: GitRetrofitService): GetStars {
        return StarsImpl(gitRetrofitService = gitRetrofitService)
    }
}