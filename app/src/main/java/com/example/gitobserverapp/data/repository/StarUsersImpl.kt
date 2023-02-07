package com.example.gitobserverapp.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.gitobserverapp.data.mapping.stargazers.DataToDomainStargazersListMapper
import com.example.gitobserverapp.data.remote.GitRetrofitService
import com.example.gitobserverapp.data.remote.model.DataStargazersListItem
import com.example.gitobserverapp.domain.model.DomainStargazersListModel
import com.example.gitobserverapp.domain.repository.GetStarUsers
import com.example.gitobserverapp.utils.Constants
import retrofit2.Response
import javax.inject.Inject

class StarUsersImpl @Inject constructor(private val gitRetrofitService: GitRetrofitService) :
    GetStarUsers {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getData(
        repo_name: String,
        owner_login: String,
        page_number: Int
    ): DomainStargazersListModel {
        val tmpList = mutableListOf<DataStargazersListItem>()
        var requestResult = loadPageAndNext(
            gitRetrofitService = gitRetrofitService,
            repo_name = repo_name,
            owner_login = owner_login,
            page_number = page_number
        )
        var tmpPage = 2
        while (requestResult.body()?.isNotEmpty() == true) {

            tmpList.addAll(requestResult.body()!!)
            requestResult = loadPageAndNext(
                gitRetrofitService = gitRetrofitService,
                repo_name = repo_name,
                owner_login = owner_login,
                page_number = tmpPage)
            tmpPage++
        }
        return DataToDomainStargazersListMapper().map(tmpList)
    }

    override suspend fun saveData(domainStargazersListModel: DomainStargazersListModel) {
        TODO("Not yet implemented")
    }

    private suspend fun loadPageAndNext(
        gitRetrofitService: GitRetrofitService,
        repo_name: String,
        owner_login: String,
        page_number: Int
    ): Response<List<DataStargazersListItem>> {
        return gitRetrofitService.getStarredData(
            repo_name = repo_name,
            owner_login = owner_login,
            per_page = Constants.MAX_PER_PAGE,
            page = page_number
        )
    }
}