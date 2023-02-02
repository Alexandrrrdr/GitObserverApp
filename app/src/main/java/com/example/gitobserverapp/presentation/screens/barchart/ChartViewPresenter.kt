package com.example.gitobserverapp.presentation.screens.barchart

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.gitobserverapp.domain.usecase.GetStargazersUseCase
import com.example.gitobserverapp.presentation.chart.model.BarChartModel
import com.example.gitobserverapp.presentation.chart.model.PresentationStargazersListItem
import com.example.gitobserverapp.presentation.chart.model.PresentationStargazersListModel
import com.example.gitobserverapp.presentation.mapping.stargazers.DomainToPresentationStargazersListMapper
import com.example.gitobserverapp.utils.Constants
import com.example.gitobserverapp.utils.Constants.START_PAGE
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import java.time.LocalDate

@InjectViewState
class ChartViewPresenter(private val getStargazersUseCase: GetStargazersUseCase): MvpPresenter<ChartView>() {

    private val tmpListBarChart = mutableListOf<BarChartModel>()
    private var lastPage = 1
    private var page = 1

    @RequiresApi(Build.VERSION_CODES.O)
    fun getStargazersList(repoName: String, repoOwnerName: String) {
        tmpListBarChart.addAll(emptyList())
        page = START_PAGE
        viewState.showLoadPage()
        CoroutineScope(Dispatchers.IO).launch {
            val tmpPresentationMapped: PresentationStargazersListModel
            val domainStargazersList = async { getStargazersUseCase.getData(
                repo_name = repoName,
                owner_login = repoOwnerName,
                page_number = START_PAGE
            )
                }
            tmpPresentationMapped = DomainToPresentationStargazersListMapper().map(domainStargazersList.await())
            tmpListBarChart.addAll(compareYearsModel(tmpPresentationMapped.stargazers_list))
            withContext(Dispatchers.Main){
                prepareListForChart(page = page)
            }
//            if (tmpPresentationMapped.stargazers_list.isNotEmpty()){
//                withContext(Dispatchers.Main) {
//                    viewState.showSuccessPage(
//                        list = prepareListForChart(page = page),
//                        lastPage = lastPage)
//                }
//            }
        }
    }

    private fun setLastPage(list: List<BarChartModel>){
        val lastItems = list.size % 5
        lastPage = if (lastItems == 0) {
            list.size / 5
        } else {
            list.size / 5 + 1
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun compareYearsModel(list: List<PresentationStargazersListItem>): List<BarChartModel> {

        var endDateYear = list[list.lastIndex].starred_at.year
        var startDateYear = list[Constants.ZERO_PAGE].starred_at.year
        var todayDateYear = LocalDate.now().year
        val matchedListForBarChartModel = mutableListOf<BarChartModel>()
        var amountOfDates = 0
        for (i in startDateYear..todayDateYear){
            amountOfDates++
        }
//        Log.d("info", "amount of dates $amountOfDates")
//        Log.d("info", "start date is $startDateYear")

        //If last stargazers starred date less than today year 2023 i fill empty data until starred
        if (todayDateYear > endDateYear){
            while (todayDateYear > endDateYear){
                matchedListForBarChartModel.add(
                    element = BarChartModel(
                        period = todayDateYear,
                        userInfo = emptyList()
                    )
                )
                todayDateYear--
            }
        }

        //stargazers started
        while (endDateYear >= startDateYear) {
            val usersForBarChartData = mutableListOf<PresentationStargazersListItem>()
            usersForBarChartData.addAll(list.filter { it.starred_at.year == endDateYear })
            matchedListForBarChartModel.add(
                element = BarChartModel(
                    period = endDateYear,
                    userInfo = usersForBarChartData
                )
            )
            endDateYear--
        }

        if (amountOfDates < 5){
            val tmpLeftDates = 5 -amountOfDates
            for (i in 1..tmpLeftDates){
                matchedListForBarChartModel.add(
                    element = BarChartModel(
                        period = startDateYear,
                        userInfo = emptyList()
                    )
                )
            }
        } else {
            val differ = amountOfDates % 5
            if (differ != 0){
                val tmpStartDay = startDateYear - (5 - differ)
                while (startDateYear > (tmpStartDay)) {
                    startDateYear--

                    matchedListForBarChartModel.add(
                        element = BarChartModel(
                            period = startDateYear,
                            userInfo = emptyList()
                        )
                    )
                }
            }
        }
        setLastPage(matchedListForBarChartModel)
        return matchedListForBarChartModel
    }

    private fun setPage(value: Int){
        page = value
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun prepareListForChart(page: Int)
//    : List<BarChartModel>
    {
        setPage(value = page)
        val tmpList = mutableListOf<BarChartModel>()

        for (i in tmpListBarChart.indices) {

            when (page) {
                1 -> {
                    tmpList.clear()
                    tmpList.addAll(tmpListBarChart.slice(0..4))
                    Log.d("info", "tmplist is ${tmpList.size}")
                    viewState.showSuccessPage(list = tmpList, lastPage = lastPage, page = page)
                    return
                }
                2 -> {
                    tmpList.clear()
                    tmpList.addAll(tmpListBarChart.slice(5..9))
                    Log.d("info", "${tmpList.size}")
                    viewState.showSuccessPage(list = tmpList, lastPage = lastPage, page = page)
                    return
                }
                3 -> {
                    tmpList.clear()
                    tmpList.addAll(tmpListBarChart.slice(10..14))
                    Log.d("info", "${tmpList.size}")
                    viewState.showSuccessPage(list = tmpList, lastPage = lastPage, page = page)
                    return
                }
                4 -> {
                    tmpList.clear()
                    tmpList.addAll(tmpListBarChart.slice(15..19))
                    Log.d("info", "${tmpList.size}")
                    viewState.showSuccessPage(list = tmpList, lastPage = lastPage, page = page)
                    return
                }
                5 -> {
                    tmpList.clear()
                    tmpList.addAll(tmpListBarChart.slice(20..24))
                    Log.d("info", "${tmpList.size}")
                    viewState.showSuccessPage(list = tmpList, lastPage = lastPage, page = page)
                    return
                }
                6 -> {
                    tmpList.clear()
                    tmpList.addAll(tmpListBarChart.slice(25..29))
                    Log.d("info", "page is $page last page is $lastPage")
                    viewState.showSuccessPage(list = tmpList, lastPage = lastPage, page = page)
                    return
                }
            }
        }
//        return tmpList
    }
}