package com.example.gitobserverapp.domain.usecase

import com.example.gitobserverapp.domain.model.DomainStargazersListModel
import com.example.gitobserverapp.domain.repository.DomainGetStargazersRepository
import com.example.gitobserverapp.domain.usecase.base.BaseUseCaseWithParams
import javax.inject.Inject

class GetStargazersUseCase @Inject constructor(
    private val getStargazersRepository: DomainGetStargazersRepository,
): BaseUseCaseWithParams<String, String, Int, DomainStargazersListModel> {

    override suspend fun getData(
        value_one: String,
        value_two: String,
        value_three: Int
    ): DomainStargazersListModel {
        return getStargazersRepository.getData(
            owner_login = value_one,
            repo_name = value_two,
            page = value_three
        )
    }
}