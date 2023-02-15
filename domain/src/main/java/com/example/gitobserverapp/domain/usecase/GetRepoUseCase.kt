package com.example.gitobserverapp.domain.usecase

import com.example.gitobserverapp.domain.model.interfacesvar.Repo
import com.example.gitobserverapp.domain.model.NetworkState
import com.example.gitobserverapp.domain.repository.GetRepos
import com.example.gitobserverapp.domain.usecase.base.BaseUseCaseRepoParams
import javax.inject.Inject

class GetRepoUseCase @Inject constructor(
    private val getRepos: GetRepos,
): BaseUseCaseRepoParams<String, Int, NetworkState<List<Repo>>> {

    override suspend fun getData(repoName: String, pageNumber: Int): NetworkState<List<Repo>> {
        return getRepos.getData(searchWord = repoName, page = pageNumber)
    }
}