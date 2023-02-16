package com.example.gitobserverapp.domain.usecase

import com.example.gitobserverapp.domain.model.NetworkState
import com.example.gitobserverapp.domain.model.StarUser
import com.example.gitobserverapp.domain.repository.GetRepository
import com.example.gitobserverapp.domain.usecase.base.BaseStarGroupUseCase
import javax.inject.Inject

class GetStarUseCase @Inject constructor(
    private val getRepository: GetRepository,
) : BaseStarGroupUseCase<String, String, Int, NetworkState<List<StarUser>>> {

    override suspend fun getStarGroup(
        repoName: String,
        ownerLogin: String,
        pageNumber: Int
    ): NetworkState<List<StarUser>> {
        return getRepository.getStarGroup(
            repoName = repoName,
            ownerLogin = ownerLogin,
            pageNumber = pageNumber
        )
    }
}