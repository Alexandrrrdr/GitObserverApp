package com.example.gitobserverapp.di

import com.example.gitobserverapp.domain.repository.DomainGetRepoByNameRepository
import com.example.gitobserverapp.domain.repository.DomainGetStargazersRepository
import com.example.gitobserverapp.domain.usecase.GetReposUseCase
import com.example.gitobserverapp.domain.usecase.GetStargazersUseCase
import dagger.Module
import dagger.Provides

@Module
class DomainModul {

    @Provides
    fun provideGetReposUseCase(domainGetRepoByNameRepository: DomainGetRepoByNameRepository): GetReposUseCase{
        return GetReposUseCase(domainGetRepoByNameRepository = domainGetRepoByNameRepository)
    }

    @Provides
    fun provideGetStargazersUseCase(domainGetStargazersRepository: DomainGetStargazersRepository): GetStargazersUseCase{
        return GetStargazersUseCase(getStargazersRepository = domainGetStargazersRepository)
    }
}