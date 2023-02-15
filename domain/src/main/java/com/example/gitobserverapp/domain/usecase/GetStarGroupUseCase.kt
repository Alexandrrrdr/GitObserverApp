package com.example.gitobserverapp.domain.usecase

import com.example.gitobserverapp.domain.model.NetworkState
import com.example.gitobserverapp.domain.repository.GetStars
import com.example.gitobserverapp.domain.usecase.base.BaseUseCaseStargazers
import javax.inject.Inject

class GetStarGroupUseCase @Inject constructor(
    private val getStars: GetStars,
) : BaseUseCaseStargazers<String, String, Int, NetworkState<List<StarGroup>>> {

    override suspend fun getData(
        repoName: String,
        ownerLogin: String,
        pageNumber: Int
    ): NetworkState<List<StarGroup>> {
        return getStars.getData(
            repoName = repoName,
            ownerLogin = ownerLogin,
            pageNumber = pageNumber
        )
    }
}