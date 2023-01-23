package com.example.gitobserverapp.data.repository

import com.example.gitobserverapp.data.network.GitRetrofitService
import com.example.gitobserverapp.domain.model.DomainReposListModel
import com.example.gitobserverapp.domain.model.Items
import com.example.gitobserverapp.domain.repository.DomainGetRepoByNameRepository
import com.example.gitobserverapp.utils.ApiResult
import com.example.gitobserverapp.utils.Constants
import javax.inject.Inject

class ReposRepositoryImpl @Inject constructor(var gitRetrofitService: GitRetrofitService) :
    DomainGetRepoByNameRepository {

    override suspend fun getData(searchWord: String, page: Int): DomainReposListModel {
        val tmpList = mutableListOf<Items>()
        val apiResult = gitRetrofitService.getRepos(
            q = searchWord,
            sort = Constants.SORT_BY,
            page = page,
            per_page = Constants.MAX_PER_PAGE)
        if (apiResult.isSuccessful && apiResult.body() != null){
            for (i in apiResult.body()!!.items.indices){
                val value = Items(created_at = apiResult.body()!!.items[i].created_at,
                    repo_name = apiResult.body()!!.items[i].name,
                    stargazers_count = apiResult.body()!!.items[i].stargazers_count,
                    owner_avatar_url = apiResult.body()!!.items[i].owner.avatar_url,
                    owner_login = apiResult.body()!!.items[i].owner.login,
                    owner_id = apiResult.body()!!.items[i].owner.id
                )
                tmpList.add(i, value)
            }
        }
        return DomainReposListModel(tmpList)
    }

    override suspend fun saveData(domainReposListModel: DomainReposListModel) {
        TODO("Not yet implemented")
    }
}