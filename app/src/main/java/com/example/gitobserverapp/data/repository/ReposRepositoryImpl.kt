package com.example.gitobserverapp.data.repository

import android.content.Context
import android.util.Log
import com.example.gitobserverapp.data.mapping.repos.DataToDomainRepoListMapper
import com.example.gitobserverapp.data.network.GitRetrofitService
import com.example.gitobserverapp.domain.model.DomainReposListModel
import com.example.gitobserverapp.domain.model.Items
import com.example.gitobserverapp.domain.repository.DomainGetRepoByNameRepository
import com.example.gitobserverapp.utils.Constants
import com.example.gitobserverapp.utils.network.NetworkStatusHelper
import javax.inject.Inject

class ReposRepositoryImpl @Inject constructor(
    private val gitRetrofitService: GitRetrofitService,
    private val networkStatusHelper: NetworkStatusHelper
) :
    DomainGetRepoByNameRepository {

    override suspend fun getData(searchWord: String, page: Int): DomainReposListModel {
        val tmpList = emptyList<Items>()

        //TODO doesn't work network checker
        val network  = networkStatusHelper.checkNetwork()

            val apiResult = gitRetrofitService.getRepos(
                q = searchWord,
                sort = Constants.SORT_BY,
                page = page,
                per_page = Constants.DEF_PER_PAGE
            )
            when(apiResult.code()){
                200 -> return DataToDomainRepoListMapper().map(apiResult.body()!!)
                in 201..500 -> return DomainReposListModel(hasNetwork = false, emptyList())
            }

        return DomainReposListModel(hasNetwork = true, tmpList)
    }

    override suspend fun saveData(domainReposListModel: DomainReposListModel) {
        TODO("Not yet implemented")
    }

}