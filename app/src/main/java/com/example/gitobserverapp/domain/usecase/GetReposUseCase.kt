package com.example.gitobserverapp.domain.usecase

import com.example.gitobserverapp.data.remote.model.GitResponse
import com.example.gitobserverapp.data.remote.model.RemoteRepo
import com.example.gitobserverapp.domain.repository.GetRepoListByName
import com.example.gitobserverapp.domain.usecase.base.BaseUseCaseRepoParams
import javax.inject.Inject

class GetReposUseCase @Inject constructor(
    private val getRepoListByName: GetRepoListByName,
): BaseUseCaseRepoParams<String, Int, GitResponse<List<RemoteRepo>>> {

    override suspend fun getData(repo_name: String, page_number: Int): GitResponse<List<RemoteRepo>> {
        return getRepoListByName.getData(searchWord = repo_name, page = page_number)
    }

}