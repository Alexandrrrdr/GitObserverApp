package com.example.gitobserverapp.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.gitobserverapp.data.mapping.stargazers.DataToDomainStargazersListMapper
import com.example.gitobserverapp.data.network.GitRetrofitService
import com.example.gitobserverapp.data.network.model.DataStargazersListItem
import com.example.gitobserverapp.domain.model.DomainStargazersListModel
import com.example.gitobserverapp.domain.repository.DomainGetStargazersRepository
import com.example.gitobserverapp.utils.Constants
import retrofit2.Response
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

//            Log.d("info", requestResult.body()!!.size.toString())

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

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun checkLoadedPage(list: DataStargazersListModel) {
//        if (list.isNotEmpty()) {
//
//        } else {
//            parseChartData(requestBodyList, repoName = searchLiveData[Constants.ZERO_PAGE].repoName)
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun loadNewPage() {
//        page++
//        getStargazersList()
//    }
//
//    //Parse date format from String to LocalDate
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun parseChartData(starredDataList: List<ListStargazersModel>, repoName: String) {
//        val starParsedList = mutableListOf<StargazerModel>()
//        var starredModel: StargazerModel
//
//        for (i in starredDataList.indices) {
//            val localDate = dateConverter(starredDataList[i].data[i].starred_at)
//            starredModel = StargazerModel(
//                user = starredDataList[i].data[i].user,
//                starredAt = localDate,
//                repoName = repoName
//            )
//            starParsedList.add(i, starredModel)
//        }
//        _chartScreenState.postValue(ChartViewState.ViewContentMain)
//        compareYearsModel(starParsedList)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun compareYearsModel(list: List<StargazerModel>) {
//
//        val tmpMatchedList = mutableListOf<BarChartModel>()
//        val endDate = list[list.lastIndex].starredAt.year
//        var startDate = list[Constants.ZERO_PAGE].starredAt.year
//
//        while (startDate <= endDate) {
//            val tmpUsers = mutableListOf<User>()
//            val list1 = list.filter { it.starredAt.year == startDate }
//            for (i in list1.indices) {
//                tmpUsers.add(i, list1[i].user)
//            }
//            tmpMatchedList.add(
//                element = BarChartModel(
//                    period = startDate,
//                    userInfo = tmpUsers
//                )
//            )
//            startDate++
//        }
////        tmpMatchedList.addAll(tmpMatchedList.sortedBy { it.period })
//        setBarChartYearsData(tmpMatchedList)
//    }
//

}