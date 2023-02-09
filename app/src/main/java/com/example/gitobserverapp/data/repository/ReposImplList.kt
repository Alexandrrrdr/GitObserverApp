package com.example.gitobserverapp.data.repository

import com.example.gitobserverapp.data.remote.GitRetrofitService
import com.example.gitobserverapp.data.remote.GitResponse
import com.example.gitobserverapp.data.remote.model.RemoteRepoResult
import com.example.gitobserverapp.domain.repository.GetRepoListByName
import com.example.gitobserverapp.utils.BaseRepo
import com.example.gitobserverapp.utils.Constants.DEF_PER_PAGE
import com.example.gitobserverapp.utils.Constants.SORT_BY
import javax.inject.Inject

class ReposImplList @Inject constructor(
    private val gitRetrofitService: GitRetrofitService
) : GetRepoListByName, BaseRepo() {

    override suspend fun getData(searchWord: String, page: Int): GitResponse<RemoteRepoResult> {
        return handleResponse { gitRetrofitService.getRepos(
                q = searchWord,
                sort = SORT_BY,
                page = page,
                per_page = DEF_PER_PAGE)
        }
    }

    override suspend fun saveData(gitResult: GitResponse<RemoteRepoResult>) {
        TODO("Not yet implemented")
    }
}