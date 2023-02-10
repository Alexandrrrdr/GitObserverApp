package com.example.gitobserverapp.data.repository

import com.example.gitobserverapp.data.remote.GitRetrofitService
import com.example.gitobserverapp.data.remote.GitResponse
import com.example.gitobserverapp.data.remote.model.RemoteRepoResultList
import com.example.gitobserverapp.data.utils.BaseRepo
import com.example.gitobserverapp.data.utils.Constants.DEF_PER_PAGE
import com.example.gitobserverapp.data.utils.Constants.SORT_BY
import com.example.gitobserverapp.domain.repository.GetRepos
import javax.inject.Inject

class ReposImplList @Inject constructor(
    private val gitRetrofitService: GitRetrofitService
) : GetRepos, BaseRepo() {

    override suspend fun getData(searchWord: String, page: Int): GitResponse<RemoteRepoResultList> {
        return handleResponse { gitRetrofitService.getRepos(
                q = searchWord,
                sort = SORT_BY,
                page = page,
                per_page = DEF_PER_PAGE)
        }
    }

    override suspend fun saveData(gitResult: GitResponse<RemoteRepoResultList>) {
        TODO("Not yet implemented")
    }
}