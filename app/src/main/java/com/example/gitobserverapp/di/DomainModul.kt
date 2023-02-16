package com.example.gitobserverapp.di

import com.example.gitobserverapp.domain.repository.GetRepository
import com.example.gitobserverapp.domain.usecase.GetReposUseCase
import com.example.gitobserverapp.domain.usecase.GetStarUseCase
import dagger.Module
import dagger.Provides

@Module
class DomainModul {

    @Provides
    fun provideGetReposUseCase(getRepository: GetRepository): GetReposUseCase {
        return GetReposUseCase(getRepository = getRepository)
    }

    @Provides
    fun provideGetStargazersUseCase(getRepository: GetRepository): GetStarUseCase {
        return GetStarUseCase(getRepository = getRepository)
    }
}