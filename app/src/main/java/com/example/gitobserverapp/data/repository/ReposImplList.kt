package com.example.gitobserverapp.data.repository

import com.example.gitobserverapp.data.remote.GitRetrofitService
import com.example.gitobserverapp.data.remote.model.GitResponse
import com.example.gitobserverapp.data.remote.model.RemoteRepo
import com.example.gitobserverapp.domain.model.DomainReposListModel
import com.example.gitobserverapp.domain.repository.GetRepoListByName
import com.example.gitobserverapp.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReposImplList @Inject constructor(
    private val gitRetrofitService: GitRetrofitService
) : GetRepoListByName {

    override suspend fun getData(searchWord: String, page: Int): GitResponse<List<RemoteRepo>> {
        return try {
            val apiResult = gitRetrofitService.getRepos(
                q = searchWord,
                sort = Constants.SORT_BY,
                page = page,
                per_page = Constants.DEF_PER_PAGE
            )
            GitResponse.Success(data = apiResult.body()!!)
        } catch (e: Exception){
            GitResponse.Error(data = null, message = e.message.toString())
        }
    }

    override suspend fun saveData(domainReposListModel: DomainReposListModel) {
        TODO("Not yet implemented")
    }

}