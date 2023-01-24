package com.example.gitobserverapp.data.repository

import com.example.gitobserverapp.data.mapping.stargazers.DataToDomainStargazersListMapper
import com.example.gitobserverapp.data.network.GitRetrofitService
import com.example.gitobserverapp.domain.model.DomainStargazersListModel
import com.example.gitobserverapp.domain.model.StargazersList
import com.example.gitobserverapp.domain.repository.DomainGetStargazersRepository
import com.example.gitobserverapp.utils.Constants

class StargazersRepositoryImpl(private var gitRetrofitService: GitRetrofitService):
    DomainGetStargazersRepository {

    override suspend fun getData(
        owner_login: String,
        repo_name: String,
        page: Int
    ): DomainStargazersListModel {
        val tmpList = emptyList<StargazersList>()
        val apiResult = gitRetrofitService.getStarredData(
            owner_login = owner_login,
            repo_name = repo_name,
            per_page = Constants.MAX_PER_PAGE,
            page = page
        )
        if (apiResult.isSuccessful && apiResult.body() != null) {
            return DataToDomainStargazersListMapper().map(apiResult.body()!!)
        }
        return DomainStargazersListModel(tmpList)
    }

        override suspend fun saveData(domainStargazersListModel: DomainStargazersListModel) {
            TODO("Not yet implemented")
        }

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun checkLoadedPage(list: StargazersListModel) {
//
////        val startDate = dateConverter(requestBodyList[ZERO_PAGE].starred_at)
////        val endDate = dateConverter(requestBodyList[requestBodyList.lastIndex].starred_at)
//        if (list.isNotEmpty()) {
//            requestBodyList.addAll(list)
//            loadNewPage()
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
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun dateConverter(value: String): LocalDate {
//        val instant = Instant.parse(value)
//        return LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.id)).toLocalDate()
//    }
}