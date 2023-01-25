package com.example.gitobserverapp.domain.usecase

import com.example.gitobserverapp.domain.model.DomainStargazersListModel
import com.example.gitobserverapp.domain.repository.DomainGetStargazersRepository
import com.example.gitobserverapp.domain.usecase.base.BaseUseCaseStargazers

class GetStargazersUseCase(
    private val getStargazersRepository: DomainGetStargazersRepository,
) : BaseUseCaseStargazers<String, String, Int, DomainStargazersListModel> {

    override suspend fun getData(
        repo_name: String,
        owner_login: String,
        page_number: Int
    ): DomainStargazersListModel {
        return getStargazersRepository.getData(
            repo_name = repo_name,
            owner_login = owner_login,
            page_number = page_number
        )
    }
}