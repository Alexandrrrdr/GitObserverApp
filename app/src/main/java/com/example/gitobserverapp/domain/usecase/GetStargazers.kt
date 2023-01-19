package com.example.gitobserverapp.domain.usecase

import com.example.gitobserverapp.domain.repository.DomainGetStargazersRepository

class GetStargazers(private val getStargazersRepository: DomainGetStargazersRepository) {

    suspend operator fun invoke(getStargazersRepository: DomainGetStargazersRepository){
        getStargazersRepository.getData()
    }
}