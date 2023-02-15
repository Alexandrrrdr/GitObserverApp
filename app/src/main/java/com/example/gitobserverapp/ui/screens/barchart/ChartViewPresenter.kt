package com.example.gitobserverapp.ui.screens.barchart

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.gitobserverapp.domain.model.NetworkState
import com.example.gitobserverapp.domain.usecase.GetStarGroupUseCase
import com.example.gitobserverapp.ui.screens.barchart.model.BarChartModel
import com.example.gitobserverapp.ui.screens.barchart.model.UiStarGroup
import com.example.gitobserverapp.utils.Constants
import com.example.gitobserverapp.utils.Constants.START_PAGE
import com.example.gitobserverapp.utils.mapper.UiStarGroupMapper
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

@InjectViewState
class ChartViewPresenter
@Inject constructor(
    private val getStarGroupUseCase: GetStarGroupUseCase,
    private val uiStarGroupMapper: UiStarGroupMapper): MvpPresenter<ChartView>() {

    private val tmpListBarChart = mutableListOf<BarChartModel>()
    private var lastPage = 1
    private var page = 1

    @RequiresApi(Build.VERSION_CODES.O)
    fun getStargazersList(repoName: String, repoOwnerName: String) {

        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }
        tmpListBarChart.addAll(emptyList())
        page = START_PAGE
        viewState.showLoadPage()
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val starList = getStarGroupUseCase.getData(
                repo_name = repoName,
                owner_login = repoOwnerName,
                page_number = START_PAGE
            )
                when (starList) {
                    is NetworkState.Error -> withContext(Dispatchers.Main){viewState.showErrorPage(starList.error)}
                    is NetworkState.HttpErrors.BadGateWay -> withContext(Dispatchers.Main){viewState.showErrorPage(starList.exception)}
                    is NetworkState.HttpErrors.InternalServerError -> withContext(Dispatchers.Main){viewState.showErrorPage(starList.exception)}
                    is NetworkState.HttpErrors.RemovedResourceFound -> withContext(Dispatchers.Main){viewState.showErrorPage(starList.exception)}
                    is NetworkState.HttpErrors.ResourceForbidden -> withContext(Dispatchers.Main){viewState.showErrorPage(starList.exception)}
                    is NetworkState.HttpErrors.ResourceNotFound -> withContext(Dispatchers.Main){viewState.showErrorPage(starList.exception)}
                    is NetworkState.HttpErrors.ResourceRemoved -> withContext(Dispatchers.Main){viewState.showErrorPage(starList.exception)}
                    NetworkState.InvalidData -> withContext(Dispatchers.Main){viewState.showErrorPage("Something wrong with data from server")}
                    is NetworkState.NetworkException -> withContext(Dispatchers.Main){viewState.showNetworkErrorPage()}
                    is NetworkState.Success -> {
                        withContext(Dispatchers.Main) {
                            tmpListBarChart.addAll(compareYearsModel(uiStarGroupMapper.mapStarGroupList(starList.data)))
                            prepareListForChart(START_PAGE)
                        }
                    }
                }
        }
    }


    private fun setLastPage(list: List<BarChartModel>) {
        val lastItems = list.size % 5
        lastPage = if (lastItems == 0) {
            list.size / 5
        } else {
            list.size / 5 + 1
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun compareYearsModel(list: List<UiStarGroup>): List<BarChartModel> {

        var endDateYear = convertToLocalDateViaInstant(list[list.lastIndex].date)!!.year

        var startDateYear = convertToLocalDateViaInstant(list[Constants.ZERO_PAGE].date)!!.year
        var todayDateYear = LocalDate.now().year
        val matchedListForBarChartModel = mutableListOf<BarChartModel>()
        var amountOfDates = 0
        for (i in startDateYear..todayDateYear) {
            amountOfDates++
        }

        //If last stargazers starred date less than today year 2023 i fill empty data until starred
        if (todayDateYear > endDateYear) {
            while (todayDateYear > endDateYear) {
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
            val usersForBarChartData = mutableListOf<UiStarGroup>()
            usersForBarChartData.addAll(list.filter { convertToLocalDateViaInstant(it.date)!!.year == endDateYear })
            matchedListForBarChartModel.add(
                element = BarChartModel(
                    period = endDateYear,
                    userInfo = usersForBarChartData
                )
            )
            endDateYear--
        }

        if (amountOfDates < 5) {
            val tmpLeftDates = 5 - amountOfDates
            for (i in 1..tmpLeftDates) {
                matchedListForBarChartModel.add(
                    element = BarChartModel(
                        period = startDateYear,
                        userInfo = emptyList()
                    )
                )
            }
        } else {
            val differ = amountOfDates % 5
            if (differ != 0) {
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertToLocalDateViaInstant(dateToConvert: Date): LocalDate? {
        return dateToConvert.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun prepareListForChart(page: Int) {
        this.page = page
        val tmpList = mutableListOf<BarChartModel>()

        when (page) {
            1 -> {
                tmpList.addAll(emptyList())
                tmpList.addAll(tmpListBarChart.slice(0..4))
                viewState.showSuccessPage(list = tmpList, lastPage = lastPage, page = page)
            }
            2 -> {
                tmpList.addAll(emptyList())
                tmpList.addAll(tmpListBarChart.slice(5..9))
                viewState.showSuccessPage(list = tmpList, lastPage = lastPage, page = page)
            }
            3 -> {
                tmpList.addAll(emptyList())
                tmpList.addAll(tmpListBarChart.slice(10..14))
                viewState.showSuccessPage(list = tmpList, lastPage = lastPage, page = page)
            }
            4 -> {
                tmpList.addAll(emptyList())
                tmpList.addAll(tmpListBarChart.slice(15..19))
                viewState.showSuccessPage(list = tmpList, lastPage = lastPage, page = page)
            }
            5 -> {
                tmpList.addAll(emptyList())
                tmpList.addAll(tmpListBarChart.slice(20..24))
                viewState.showSuccessPage(list = tmpList, lastPage = lastPage, page = page)
            }
            6 -> {
                tmpList.addAll(emptyList())
                tmpList.addAll(tmpListBarChart.slice(25..29))
                viewState.showSuccessPage(list = tmpList, lastPage = lastPage, page = page)
            }
        }
    }
}