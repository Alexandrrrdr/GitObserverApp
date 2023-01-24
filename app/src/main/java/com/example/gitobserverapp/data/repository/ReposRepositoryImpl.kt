package com.example.gitobserverapp.data.repository

import com.example.gitobserverapp.data.mapping.repos.DataToDomainRepoListMapper
import com.example.gitobserverapp.data.network.GitRetrofitService
import com.example.gitobserverapp.domain.model.DomainReposListModel
import com.example.gitobserverapp.domain.model.Items
import com.example.gitobserverapp.domain.repository.DomainGetRepoByNameRepository
import com.example.gitobserverapp.utils.Constants

class ReposRepositoryImpl(var gitRetrofitService: GitRetrofitService) :
    DomainGetRepoByNameRepository {

    override suspend fun getData(searchWord: String, page: Int): DomainReposListModel {
        val tmpList = emptyList<Items>()
        val apiResult = gitRetrofitService.getRepos(
            q = searchWord,
            sort = Constants.SORT_BY,
            page = page,
            per_page = Constants.MAX_PER_PAGE)
        if (apiResult.isSuccessful && apiResult.body() != null){
            return DataToDomainRepoListMapper().map(apiResult.body()!!)
        }
        return DomainReposListModel(tmpList)
    }

    override suspend fun saveData(domainReposListModel: DomainReposListModel) {
        TODO("Not yet implemented")
    }
}