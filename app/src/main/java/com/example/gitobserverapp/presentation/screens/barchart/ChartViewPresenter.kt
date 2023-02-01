package com.example.gitobserverapp.presentation.screens.barchart

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.example.gitobserverapp.domain.usecase.GetStargazersUseCase
import com.example.gitobserverapp.presentation.chart.chart_helper.ChartViewState
import com.example.gitobserverapp.presentation.chart.model.BarChartModel
import com.example.gitobserverapp.presentation.chart.model.PresentationStargazersListItem
import com.example.gitobserverapp.presentation.chart.model.PresentationStargazersListModel
import com.example.gitobserverapp.presentation.mapping.stargazers.DomainToPresentationStargazersListMapper
import com.example.gitobserverapp.presentation.screens.main.MainSearchView
import com.example.gitobserverapp.utils.Constants
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import java.time.LocalDate

@InjectViewState
class ChartViewPresenter(private val getStargazersUseCase: GetStargazersUseCase): MvpPresenter<ChartView>() {

    private val tmpListBarChart = mutableListOf<BarChartModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getStargazersList(repoName: String, repoOwnerName: String, page: Int) {
        viewState.showLoadPage()
        CoroutineScope(Dispatchers.IO).launch {
            val tmpPresentationMapped: PresentationStargazersListModel
            val domainStargazersList = async { getStargazersUseCase.getData(
                repo_name = repoName,
                owner_login = repoOwnerName,
                page_number = page
            )
                }
            tmpPresentationMapped = DomainToPresentationStargazersListMapper().map(domainStargazersList.await())
            tmpListBarChart.addAll(compareYearsModel(tmpPresentationMapped.stargazers_list))

            withContext(Dispatchers.Main){
                viewState.showSuccessPage(tmpListBarChart)
            }
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
        Log.d("info", "amount of dates $amountOfDates")
        Log.d("info", "start date is $startDateYear")


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
        return matchedListForBarChartModel
    }
}