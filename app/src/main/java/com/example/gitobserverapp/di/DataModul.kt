package com.example.gitobserverapp.di

import com.example.gitobserverapp.data.remote.GitRetrofitService
import com.example.gitobserverapp.data.repository.RepositoryImpl
import com.example.gitobserverapp.domain.repository.GetRepository
import com.example.gitobserverapp.utils.periods.years.ChartYearsParser
import dagger.Module
import dagger.Provides

@Module
class DataModul {

    @Provides
    fun provideDomainReposRepository(gitRetrofitService: GitRetrofitService): GetRepository {
        return RepositoryImpl(gitRetrofitService = gitRetrofitService)
    }

    @Provides
    fun provideYearParser(): ChartYearsParser {
        return ChartYearsParser()
    }
}