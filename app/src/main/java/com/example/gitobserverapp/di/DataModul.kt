package com.example.gitobserverapp.di

import com.example.gitobserverapp.data.remote.GitRetrofitService
import com.example.gitobserverapp.data.repository.ReposImplList
import com.example.gitobserverapp.data.repository.StargazersRepositoryImpl
import com.example.gitobserverapp.domain.repository.GetRepoListByName
import com.example.gitobserverapp.domain.repository.DomainGetStargazersRepository
import dagger.Module
import dagger.Provides

@Module
class DataModul {

    @Provides
    fun provideDomainReposRepository(gitRetrofitService: GitRetrofitService): GetRepoListByName{
        return ReposImplList(gitRetrofitService = gitRetrofitService)
    }

    @Provides
    fun provideDomainStargazersRepository(gitRetrofitService: GitRetrofitService): DomainGetStargazersRepository{
        return StargazersRepositoryImpl(gitRetrofitService = gitRetrofitService)
    }
}