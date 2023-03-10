package com.example.gitobserverapp.domain.usecase

import com.example.gitobserverapp.domain.model.StarDateSorted
import com.example.gitobserverapp.domain.repository.GetRepository
import com.example.gitobserverapp.domain.usecase.base.BaseStarGroupUseCase
import javax.inject.Inject

class GetStarUseCase @Inject constructor(
    private val getRepository: GetRepository,
) : BaseStarGroupUseCase<String, String, Int, StarDateSorted> {

    override suspend fun getStarGroup(
        repoName: String,
        ownerName: String,
        lastPage: Int
    ): StarDateSorted {
        return getRepository.getStarGroup(
            repoName = repoName,
            ownerName = ownerName,
            lastPage = lastPage
        )
    }
}