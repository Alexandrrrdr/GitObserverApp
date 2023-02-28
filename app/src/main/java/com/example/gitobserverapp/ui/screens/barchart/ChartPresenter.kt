package com.example.gitobserverapp.ui.screens.barchart

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.gitobserverapp.domain.usecase.GetStarUseCase
import com.example.gitobserverapp.ui.screens.barchart.model.BarChartModel
import com.example.gitobserverapp.ui.screens.barchart.model.UiStarDate
import com.example.gitobserverapp.ui.screens.barchart.model.UiStarUser
import com.example.gitobserverapp.utils.Constants
import com.example.gitobserverapp.utils.Extensions.convertToLocalDate
import com.example.gitobserverapp.utils.periods.years.YearParser
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class ChartPresenter
@Inject constructor(
    private val getStarGroupUseCase: GetStarUseCase,
    private val yearParser: YearParser
    ) : MvpPresenter<ChartView>() {

    private val tmpListBarChart = mutableListOf<BarChartModel>()
    private val tmpListUiStarDate = mutableListOf<UiStarDate>()

    private var lastListPage = 1
    private var currentListPage = 1

    private var lastPageToLoad = 1
    private var isLoadAllowed = false

    //New version
    @RequiresApi(Build.VERSION_CODES.O)
    fun getStargazersList(repoName: String, repoOwnerName: String, page: Int) {
        this.currentListPage = page
        isLoadAllowed = lastPageToLoad > 1
        if (lastPageToLoad < 1){
            viewState.showErrorPage("No stars, check another project")
            return
        } else {
            viewState.showLoadPage()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    var starredList = loadData(repoName = repoName, ownerName = repoOwnerName, pageNumber = lastPageToLoad)
                    tmpListUiStarDate.addAll(0, starredList)
                    lastPageToLoad--

                    while (!dateRangeChecker(starredList) && lastPageToLoad >= 1){
                        starredList = loadData(repoName = repoName, ownerName = repoOwnerName, pageNumber = lastPageToLoad)
                        tmpListUiStarDate.addAll(0, starredList)
                        lastPageToLoad--
                        isLoadAllowed = lastPageToLoad > 1
                    }
                    tmpListBarChart.clear()
                    if (starredList.isNotEmpty()){
                        withContext(Dispatchers.Main) {
                            tmpListBarChart.addAll(0, yearParser.yearCreater(tmpListUiStarDate))
                            setLastListPage(tmpListBarChart)
                            navigationInList(page = page)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            viewState.showErrorPage(error = "No data from server")
                        }
                    }
                } catch (e: Exception){
                    withContext(Dispatchers.Main) {
                        viewState.showNetworkErrorPage()
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun navigationInList(page: Int) {
        this.currentListPage = page
        val tmpList = mutableListOf<BarChartModel>()
        when (page) {
            1 -> {
                tmpList.addAll(emptyList())
                tmpList.addAll(tmpListBarChart.slice(0..4))
                viewState.showSuccessPage(list = tmpList, lastPage = lastListPage, page = page, isLoadAllowed = isLoadAllowed)
            }
            2 -> {
                tmpList.addAll(emptyList())
                tmpList.addAll(tmpListBarChart.slice(5..9))
                viewState.showSuccessPage(list = tmpList, lastPage = lastListPage, page = page, isLoadAllowed = isLoadAllowed)
            }
            3 -> {
                tmpList.addAll(emptyList())
                tmpList.addAll(tmpListBarChart.slice(10..14))
                viewState.showSuccessPage(list = tmpList, lastPage = lastListPage, page = page, isLoadAllowed = isLoadAllowed)
            }
            4 -> {
                tmpList.addAll(emptyList())
                tmpList.addAll(tmpListBarChart.slice(15..19))
                viewState.showSuccessPage(list = tmpList, lastPage = lastListPage, page = page, isLoadAllowed = isLoadAllowed)
            }
            5 -> {
                tmpList.addAll(emptyList())
                tmpList.addAll(tmpListBarChart.slice(20..24))
                viewState.showSuccessPage(list = tmpList, lastPage = lastListPage, page = page, isLoadAllowed = isLoadAllowed)
            }
            6 -> {
                tmpList.addAll(emptyList())
                tmpList.addAll(tmpListBarChart.slice(25..29))
                viewState.showSuccessPage(list = tmpList, lastPage = lastListPage, page = page, isLoadAllowed = isLoadAllowed)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateRangeChecker(list: List<UiStarDate>): Boolean{
        val endDateYear = list[list.lastIndex].date.convertToLocalDate()!!.year
        val startDateYear = list[Constants.ZERO_PAGE].date.convertToLocalDate()!!.year
        val difference = (endDateYear - startDateYear) / 5
        return difference >= 1
    }

    private suspend fun loadData(
        repoName: String,
        ownerName: String,
        pageNumber: Int
    ): List<UiStarDate> {
        return getStarGroupUseCase.getStarGroup(
            repoName = repoName,
            ownerName = ownerName,
            pageNumber = pageNumber
        ).map { UiStarDate(date = it.date, user = UiStarUser(id = it.user.id, name = it.user.name, userUrl = it.user.userUrl)) }
    }

    private fun setLastListPage(list: List<BarChartModel>) {
        val lastItems = list.size % 5
        lastListPage = if (lastItems == 0) {
            list.size / 5
        } else {
            list.size / 5 + 1
        }
    }

    fun findLastLoadPage(starAmount: Int){
        if (starAmount == 0) {
            lastPageToLoad = 0
            return
        }
        val countHelper = (starAmount / 100)
        if (countHelper == 0 && starAmount != 0){
            lastPageToLoad = 1
            return
        } else if (countHelper >= 1 && (starAmount % 100) == 0){
            lastPageToLoad = countHelper
            return
        }
        lastPageToLoad = countHelper + 1
    }


//        @RequiresApi(Build.VERSION_CODES.O)
//    private fun dateInBanRange(
//        list: List<UiStarDate>
//    ): Boolean {
//        val listOfBannedDates = listOf(2019, 2014, 2009, 2004, 1999, 1994, 1989)
//        val lastDate = list[list.lastIndex].date.convertToLocalDate()!!.year
//        return (lastDate in listOfBannedDates)
//    }

//        @RequiresApi(Build.VERSION_CODES.O)
//    fun getStargazersList(repoName: String, repoOwnerName: String) {
//
//        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
//            throwable.printStackTrace()
//        }
//        tmpListBarChart.addAll(emptyList())
//        page = START_PAGE
//        viewState.showLoadPage()
//        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
//            try {
//                val starList = getStarGroupUseCase.getStarGroup(
//                    repoName = repoName,
//                    ownerName = repoOwnerName,
//                    pageNumber = START_PAGE
//                )
//                if (starList.isNotEmpty()){
//                    withContext(Dispatchers.Main) {
//                        tmpListBarChart.addAll(yearParser.compareYearsModel(starList.map {
//                            UiStarDate(
//                                date = it.date,
//                                user = UiStarUser(
//                                    id = it.user.id,
//                                    name = it.user.name,
//                                    userUrl = it.user.userUrl
//                                )
//                            )
//                        }))
//                        setLastPage(tmpListBarChart)
//                        prepareListForChart(START_PAGE)
//                    }
//                } else {
//                    withContext(Dispatchers.Main) {
//                        viewState.showErrorPage(error = "No server data")
//                    }
//                }
//            } catch (e: Exception){
//                withContext(Dispatchers.Main) {
//                    viewState.showNetworkErrorPage(e.message.toString())
//                }
//            }
//        }
//    }
}