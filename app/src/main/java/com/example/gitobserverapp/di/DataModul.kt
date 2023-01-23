package com.example.gitobserverapp.di

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

//    @Provides
//    fun provideDomainReposRepository(): DomainGetRepoByNameRepository{
//        return ReposRepositoryImpl(gitRetrofitService = GitRetrofitService())
//    }
//
//    @Provides
//    fun provideDomainStargazersRepository(): DomainGetStargazersRepository{
//        return StargazersRepositoryImpl(gitRetrofitService = GitRetrofitService())
//    }

}