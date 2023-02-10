package com.example.gitobserverapp.di

import dagger.Module
import dagger.Provides

@Module
class DataModul {

    @Provides
    fun provideDomainReposRepository(gitRetrofitService: com.example.gitobserverapp.data.remote.GitRetrofitService): com.example.gitobserverapp.domain.repository.GetRepos {
        return com.example.gitobserverapp.data.repository.ReposImplList(gitRetrofitService = gitRetrofitService)
    }

    @Provides
    fun provideDomainStargazersRepository(gitRetrofitService: com.example.gitobserverapp.data.remote.GitRetrofitService): com.example.gitobserverapp.domain.repository.GetStars {
        return com.example.gitobserverapp.data.repository.StarsImpl(gitRetrofitService = gitRetrofitService)
    }
}