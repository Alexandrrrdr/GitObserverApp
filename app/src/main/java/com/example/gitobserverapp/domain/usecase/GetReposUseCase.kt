package com.example.gitobserverapp.domain.usecase

import com.example.gitobserverapp.domain.model.DomainReposListModel
import com.example.gitobserverapp.domain.repository.DomainGetRepoByNameRepository
import com.example.gitobserverapp.domain.usecase.base.BaseUseCaseWithParams
import javax.inject.Inject

class GetReposUseCase @Inject constructor(
    private val domainGetRepoByNameRepository: DomainGetRepoByNameRepository,
): BaseUseCaseWithParams<String, String, Int, DomainReposListModel> {

    override suspend fun getData(
        value_one: String,
        value_two: String,
        value_three: Int
    ): DomainReposListModel {
        return domainGetRepoByNameRepository.getData(searchWord = value_one, page = value_three)
    }
}