package com.example.gitobserverapp.domain.usecase

import com.example.gitobserverapp.data.remote.GitResponse
import com.example.gitobserverapp.data.remote.model.RemoteRepoResultList
import com.example.gitobserverapp.domain.repository.GetRepoListByName
import com.example.gitobserverapp.domain.usecase.base.BaseUseCaseRepoParams
import javax.inject.Inject

class GetReposUseCase @Inject constructor(
    private val getRepoListByName: GetRepoListByName,
): BaseUseCaseRepoParams<String, Int, GitResponse<RemoteRepoResultList>> {

    override suspend fun getData(repo_name: String, page_number: Int): GitResponse<RemoteRepoResultList> {
        return getRepoListByName.getData(searchWord = repo_name, page = page_number)
    }
}