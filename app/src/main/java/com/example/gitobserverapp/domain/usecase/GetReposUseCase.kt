package com.example.gitobserverapp.domain.usecase

import com.example.gitobserverapp.data.remote.GitResponse
import com.example.gitobserverapp.data.remote.model.RemoteRepoResult
import com.example.gitobserverapp.domain.repository.GetRepoListByName
import com.example.gitobserverapp.domain.usecase.base.BaseUseCaseRepoParams
import javax.inject.Inject

class GetReposUseCase @Inject constructor(
    private val getRepoListByName: GetRepoListByName,
): BaseUseCaseRepoParams<String, Int, GitResponse<RemoteRepoResult>> {

    override suspend fun getData(repo_name: String, page_number: Int): GitResponse<RemoteRepoResult> {
        return getRepoListByName.getData(searchWord = repo_name, page = page_number)
    }
}