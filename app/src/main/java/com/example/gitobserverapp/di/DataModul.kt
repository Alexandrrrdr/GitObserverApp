package com.example.gitobserverapp.di

import com.example.gitobserverapp.data.remote.GitRetrofitService
import com.example.gitobserverapp.data.remote.model.GitResponse
import com.example.gitobserverapp.data.repository.ReposImplList
import com.example.gitobserverapp.data.repository.StarUsersImpl
import com.example.gitobserverapp.domain.repository.GetRepoListByName
import com.example.gitobserverapp.domain.repository.GetStarUsers
import dagger.Module
import dagger.Provides
import retrofit2.HttpException
import retrofit2.Response

@Module
class DataModul {

    @Provides
    fun provideDomainReposRepository(gitRetrofitService: GitRetrofitService): GetRepoListByName{
        return ReposImplList(gitRetrofitService = gitRetrofitService)
    }

    @Provides
    fun provideDomainStargazersRepository(gitRetrofitService: GitRetrofitService): GetStarUsers{
        return StarUsersImpl(gitRetrofitService = gitRetrofitService)
    }
}