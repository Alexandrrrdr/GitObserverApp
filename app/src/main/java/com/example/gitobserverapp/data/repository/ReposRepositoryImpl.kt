package com.example.gitobserverapp.data.repository

import com.example.gitobserverapp.data.mapping.repos.DataToDomainRepoListMapper
import com.example.gitobserverapp.data.network.GitRetrofitService
import com.example.gitobserverapp.domain.model.DomainReposListModel
import com.example.gitobserverapp.domain.model.Items
import com.example.gitobserverapp.domain.repository.DomainGetRepoByNameRepository
import com.example.gitobserverapp.presentation.chart.chart_helper.ChartViewState
import com.example.gitobserverapp.presentation.main.main_helper.MainViewState
import com.example.gitobserverapp.utils.Constants
import javax.inject.Inject

class ReposRepositoryImpl @Inject constructor(var gitRetrofitService: GitRetrofitService):
    DomainGetRepoByNameRepository {

    override suspend fun getData(searchWord: String, page: Int): DomainReposListModel {
        val tmpList = emptyList<Items>()
        val apiResult = gitRetrofitService.getRepos(
            q = searchWord,
            sort = Constants.SORT_BY,
            page = page,
            per_page = Constants.DEF_PER_PAGE)
        when(apiResult.code()){
            //Ok
            200 -> {
                if (apiResult.isSuccessful && apiResult.body() != null){
                    return DataToDomainRepoListMapper().map(apiResult.body()!!)
                } else {
                    DomainReposListModel(tmpList)
                }
            }
            //Validation failed, or the endpoint has been spammed
            422 -> {
                MainViewState.Error("Validation failed, or the endpoint has been spammed")
            }
            //Requires authentication
            401 -> {
                MainViewState.Error("Requires authentication")
            }
            //Forbidden
            403 -> {
                MainViewState.Error("Forbidden")
            }
            else -> {
                MainViewState.Error("Some error")
            }
        }
        return DomainReposListModel(tmpList)
    }

    override suspend fun saveData(domainReposListModel: DomainReposListModel) {
        TODO("Not yet implemented")
    }
}