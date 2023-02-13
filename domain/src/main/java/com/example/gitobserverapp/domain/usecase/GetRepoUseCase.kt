package com.example.gitobserverapp.domain.usecase

import com.example.gitobserverapp.domain.model.Repo
import com.example.gitobserverapp.domain.model.NetworkState
import com.example.gitobserverapp.domain.repository.GetRepos
import com.example.gitobserverapp.domain.usecase.base.BaseUseCaseRepoParams
import javax.inject.Inject

class GetRepoUseCase @Inject constructor(
    private val getRepos: GetRepos,
): BaseUseCaseRepoParams<String, Int, NetworkState<List<Repo>>> {

    override suspend fun getData(repo_name: String, page_number: Int): NetworkState<List<Repo>> {
        return getRepos.getData(searchWord = repo_name, page = page_number)
    }
}