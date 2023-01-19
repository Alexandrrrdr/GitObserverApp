package com.example.gitobserverapp.domain.usecase

import com.example.gitobserverapp.domain.repository.DomainGetDataByRepoNameRepository

class GetDataBySearchName(private val domainGetDataByRepoNameRepository: DomainGetDataByRepoNameRepository) {
    suspend operator fun invoke(domainGetDataByRepoNameRepository: DomainGetDataByRepoNameRepository){
        domainGetDataByRepoNameRepository.getData()
    }
}