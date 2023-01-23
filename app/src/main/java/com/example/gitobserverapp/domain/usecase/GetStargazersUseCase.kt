package com.example.gitobserverapp.domain.usecase

import com.example.gitobserverapp.domain.model.DomainStargazersListModel
import com.example.gitobserverapp.domain.repository.DomainGetStargazersRepository
import javax.inject.Inject

class GetStargazersUseCase @Inject constructor(
    private val getStargazersRepository: DomainGetStargazersRepository,
) {
    suspend operator fun invoke(
        owner_login: String,
        repo_name: String,
        page: Int
    ): DomainStargazersListModel {
        return getStargazersRepository.getData(
            owner_login = owner_login,
            repo_name = repo_name,
            page = page
        )
    }
}