package com.example.gitobserverapp.domain.usecase

import com.example.gitobserverapp.domain.model.StarGroup
import com.example.gitobserverapp.domain.model.NetworkState
import com.example.gitobserverapp.domain.repository.GetStars
import com.example.gitobserverapp.domain.usecase.base.BaseUseCaseStargazers
import javax.inject.Inject

class GetStarGroupUseCase @Inject constructor(
    private val getStars: GetStars,
) : BaseUseCaseStargazers<String, String, Int, NetworkState<List<StarGroup>>> {

    override suspend fun getData(
        repo_name: String,
        owner_login: String,
        page_number: Int
    ): NetworkState<List<StarGroup>> {
        return getStars.getData(
            repo_name = repo_name,
            owner_login = owner_login,
            page_number = page_number
        )
    }
}