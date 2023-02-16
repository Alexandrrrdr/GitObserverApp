package com.example.gitobserverapp.di

import com.example.gitobserverapp.data.remote.GitRetrofitService
import com.example.gitobserverapp.data.repository.RepositoryImplList
import com.example.gitobserverapp.domain.repository.GetRepository
import dagger.Module
import dagger.Provides

@Module
class DataModul {

    @Provides
    fun provideDomainReposRepository(gitRetrofitService: GitRetrofitService): GetRepository {
        return RepositoryImplList(gitRetrofitService = gitRetrofitService)
    }
}