package com.example.gitobserverapp.di

import android.content.Context
import com.example.gitobserverapp.data.network.GitRetrofitService
import com.example.gitobserverapp.data.repository.ReposRepositoryImpl
import com.example.gitobserverapp.data.repository.StargazersRepositoryImpl
import com.example.gitobserverapp.domain.repository.DomainGetRepoByNameRepository
import com.example.gitobserverapp.domain.repository.DomainGetStargazersRepository
import com.example.gitobserverapp.utils.network.NetworkStatusHelper
import dagger.Module
import dagger.Provides

@Module
class DataModul {

    @Provides
    fun provideDomainReposRepository(gitRetrofitService: GitRetrofitService, networkStatusHelper: NetworkStatusHelper): DomainGetRepoByNameRepository{
        return ReposRepositoryImpl(gitRetrofitService = gitRetrofitService, networkStatusHelper = networkStatusHelper)
    }

    @Provides
    fun provideDomainStargazersRepository(gitRetrofitService: GitRetrofitService): DomainGetStargazersRepository{
        return StargazersRepositoryImpl(gitRetrofitService = gitRetrofitService)
    }
}