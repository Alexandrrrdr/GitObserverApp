package com.example.gitobserverapp.data.repository

import android.content.Context
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
    private val context: Context,
    private val networkStatusHelper: NetworkStatusHelper
) :
    DomainGetRepoByNameRepository {

    override suspend fun getData(searchWord: String, page: Int): DomainReposListModel {
        val tmpList = emptyList<Items>()

        if (!networkStatusHelper.checkNetwork()) {
            DomainReposListModel(hasNetwork = false, tmpList)
        }
        val apiResult = gitRetrofitService.getRepos(
            q = searchWord,
            sort = Constants.SORT_BY,
            page = page,
            per_page = Constants.DEF_PER_PAGE
        )
        if (apiResult.code() == 200 && apiResult.isSuccessful) {
            return DataToDomainRepoListMapper().map(apiResult.body()!!)
        }
        return DomainReposListModel(hasNetwork = false, tmpList)
    }

    override suspend fun saveData(domainReposListModel: DomainReposListModel) {
        TODO("Not yet implemented")
    }

}