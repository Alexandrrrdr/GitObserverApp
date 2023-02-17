package com.example.gitobserverapp.ui.screens.barchart

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.gitobserverapp.domain.model.NetworkState
import com.example.gitobserverapp.domain.usecase.GetStarUseCase
import com.example.gitobserverapp.ui.screens.barchart.model.BarChartModel
import com.example.gitobserverapp.ui.screens.barchart.model.UiStarDate
import com.example.gitobserverapp.ui.screens.barchart.model.UiStarUser
import com.example.gitobserverapp.utils.Constants
import com.example.gitobserverapp.utils.Constants.START_PAGE
import com.example.gitobserverapp.utils.Extensions.convertToLocalDate
import com.example.gitobserverapp.utils.parse_period.years.YearParser
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import java.time.LocalDate
import javax.inject.Inject

@InjectViewState
class ChartPresenter
@Inject constructor(
    private val getStarGroupUseCase: GetStarUseCase,
    private val yearParser: YearParser
    ) : MvpPresenter<ChartView>() {

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
            val starList = getStarGroupUseCase.getStarGroup(
                repoName = repoName,
                ownerLogin = repoOwnerName,
                pageNumber = START_PAGE
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
                            tmpListBarChart.addAll(compareYearsModel(starList.data.map { UiStarDate(
                                date = it.date, user = UiStarUser(id = it.user.id, name = it.user.name, userUrl = it.user.userUrl)
                            ) }))
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
    private fun compareYearsModel(list: List<UiStarDate>): List<BarChartModel> {

        Log.d("info", "compare is started!!!")
        var endDateYear = list[list.lastIndex].date.convertToLocalDate()!!.year

        var startDateYear = list[Constants.ZERO_PAGE].date.convertToLocalDate()!!.year
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
            val usersForBarChartData = mutableListOf<UiStarDate>()
            usersForBarChartData.addAll(list.filter { it.date.convertToLocalDate()!!.year == endDateYear })
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