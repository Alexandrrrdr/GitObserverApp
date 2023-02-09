package com.example.gitobserverapp.domain.usecase

import com.example.gitobserverapp.data.remote.model.GitResponse
import com.example.gitobserverapp.data.remote.model.RemoteRepo
import com.example.gitobserverapp.data.remote.model.RemoteResult
import com.example.gitobserverapp.domain.repository.GetRepoListByName
import com.example.gitobserverapp.domain.usecase.base.BaseUseCaseRepoParams
import javax.inject.Inject

class GetReposUseCase @Inject constructor(
    private val getRepoListByName: GetRepoListByName,
): BaseUseCaseRepoParams<String, Int, GitResponse<RemoteResult>> {

    override suspend fun getData(repo_name: String, page_number: Int): GitResponse<RemoteResult> {
        return getRepoListByName.getData(searchWord = repo_name, page = page_number)
    }
}