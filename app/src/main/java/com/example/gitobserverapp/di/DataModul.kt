package com.example.gitobserverapp.di

import com.example.gitobserverapp.data.mapping.repos.DataToDomainRepoListMapper
import com.example.gitobserverapp.data.mapping.stargazers.DataToDomainStargazersListMapper
import com.example.gitobserverapp.data.network.GitRetrofitService
import com.example.gitobserverapp.data.repository.ReposRepositoryImpl
import com.example.gitobserverapp.data.repository.StargazersRepositoryImpl
import com.example.gitobserverapp.domain.repository.DomainGetRepoByNameRepository
import com.example.gitobserverapp.domain.repository.DomainGetStargazersRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
class DataModul {

    @Provides
    fun provideDomainReposRepository(gitRetrofitService: GitRetrofitService): DomainGetRepoByNameRepository{
        return ReposRepositoryImpl(gitRetrofitService = gitRetrofitService)
    }

    @Provides
    fun provideDomainStargazersRepository(gitRetrofitService: GitRetrofitService): DomainGetStargazersRepository{
        return StargazersRepositoryImpl(gitRetrofitService = gitRetrofitService)
    }
}