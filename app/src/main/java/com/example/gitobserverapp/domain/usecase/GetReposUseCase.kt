package com.example.gitobserverapp.domain.usecase

import com.example.gitobserverapp.domain.model.DomainReposListModel
import com.example.gitobserverapp.domain.repository.DomainGetRepoByNameRepository
import javax.inject.Inject

class GetReposUseCase @Inject constructor(
    private val domainGetRepoByNameRepository: DomainGetRepoByNameRepository,
) {

    suspend operator fun invoke(
        searchName: String,
        page: Int): DomainReposListModel {
        return domainGetRepoByNameRepository.getData(searchWord = searchName, page = page)
    }
}