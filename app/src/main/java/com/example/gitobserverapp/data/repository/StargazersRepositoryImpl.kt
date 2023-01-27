package com.example.gitobserverapp.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.gitobserverapp.data.mapping.stargazers.DataToDomainStargazersListMapper
import com.example.gitobserverapp.data.network.GitRetrofitService
import com.example.gitobserverapp.data.network.model.DataStargazersListItem
import com.example.gitobserverapp.domain.model.DomainStargazersListItem
import com.example.gitobserverapp.domain.model.DomainStargazersListModel
import com.example.gitobserverapp.domain.repository.DomainGetStargazersRepository
import com.example.gitobserverapp.presentation.chart.chart_helper.ChartViewState
import com.example.gitobserverapp.presentation.chart.model.BarChartModel
import com.example.gitobserverapp.presentation.chart.model.PresentationStargazersListItem
import com.example.gitobserverapp.utils.Constants
import retrofit2.Response
import java.time.LocalDate
import javax.inject.Inject

class StargazersRepositoryImpl @Inject constructor(private var gitRetrofitService: GitRetrofitService) :
    DomainGetStargazersRepository {

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