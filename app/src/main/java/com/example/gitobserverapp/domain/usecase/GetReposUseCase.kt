package com.example.gitobserverapp.domain.usecase

import com.example.gitobserverapp.domain.model.DomainReposListModel
import com.example.gitobserverapp.domain.repository.DomainGetRepoByNameRepository
import com.example.gitobserverapp.domain.repository.DomainGetStargazersRepository
import com.example.gitobserverapp.domain.usecase.base.BaseUseCaseRepoParams
import com.example.gitobserverapp.domain.usecase.base.BaseUseCaseStargazers

class GetReposUseCase (
    private val domainGetRepoByNameRepository: DomainGetRepoByNameRepository,
): BaseUseCaseRepoParams<String, Int, DomainReposListModel> {

    override suspend fun getData(
        repo_name: String,
        page_number: Int
    ): DomainReposListModel {
        return domainGetRepoByNameRepository.getData(searchWord = repo_name, page = page_number)
    }

}