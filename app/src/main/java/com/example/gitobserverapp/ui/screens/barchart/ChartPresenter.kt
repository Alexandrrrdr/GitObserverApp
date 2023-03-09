package com.example.gitobserverapp.ui.screens.barchart

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.gitobserverapp.domain.usecase.GetStarUseCase
import com.example.gitobserverapp.ui.screens.barchart.model.BarChartModel
import com.example.gitobserverapp.ui.screens.barchart.model.UiSortedStars
import com.example.gitobserverapp.ui.screens.barchart.model.UiStarDate
import com.example.gitobserverapp.ui.screens.barchart.model.UiStarUser
import com.example.gitobserverapp.utils.Constants.MAX_PER_PAGE
import com.example.gitobserverapp.utils.Constants.ZERO_INDEX
import com.example.gitobserverapp.utils.Constants.PERIOD
import com.example.gitobserverapp.utils.Constants.START_PAGE
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

    private var lastListPage = START_PAGE
    private var currentListPage = START_PAGE

    private var lastPageToLoad = START_PAGE
    private var isLoadAllowed = false

    //New version
    @RequiresApi(Build.VERSION_CODES.O)
    fun getStargazersList(repoName: String, repoOwnerName: String, page: Int) {
        this.currentListPage = page
        isLoadAllowed = lastPageToLoad > START_PAGE
        if (lastPageToLoad < START_PAGE){
            viewState.showErrorPage("No stars, check another project")
            return
        } else {
            viewState.showLoadPage()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                   val sortedStarsData = getStarGroupUseCase.getStarGroup(
                        repoName = repoName,
                        ownerName = repoOwnerName,
                        lastPage = lastPageToLoad
                    )
                    tmpListUiStarDate.addAll(ZERO_INDEX, sortedStarsData.list.map { UiStarDate(
                        date = it.date, user = UiStarUser(
                        id = it.user.id, name = it.user.name, userUrl = it.user.userUrl)) })

                    tmpListBarChart.clear()
                    if (sortedStarsData.list.isNotEmpty()){
                        withContext(Dispatchers.Main) {
                            lastPageToLoad = sortedStarsData.lastPage
                            isLoadAllowed = sortedStarsData.isLoadAvailable
                            tmpListBarChart.addAll(ZERO_INDEX, yearParser.yearCreater(tmpListUiStarDate))
                            setLastListPage(tmpListBarChart)
                            navigationHelper(page = page)
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

    fun navigationHelper(page: Int){
        val tmpList = mutableListOf<BarChartModel>()
        val start = page * PERIOD - PERIOD
        val end = page * PERIOD - START_PAGE
        tmpList.addAll(tmpListBarChart.slice(start..end))
        viewState.showSuccessPage(list = tmpList, lastPage = lastListPage, page = page, isLoadAllowed = isLoadAllowed)
    }

    private fun setLastListPage(list: List<BarChartModel>) {
        val lastItems = list.size % PERIOD
        lastListPage = if (lastItems == ZERO_INDEX) {
            list.size / PERIOD
        } else {
            list.size / PERIOD + START_PAGE
        }
    }

    fun findLastLoadPage(starAmount: Int){
        if (starAmount == ZERO_INDEX) {
            lastPageToLoad = ZERO_INDEX
            return
        }
        val countHelper = (starAmount / MAX_PER_PAGE)
        if (countHelper == ZERO_INDEX && starAmount != ZERO_INDEX){
            lastPageToLoad = START_PAGE
            return
        } else if (countHelper >= START_PAGE && (starAmount % MAX_PER_PAGE) == ZERO_INDEX){
            lastPageToLoad = countHelper
            return
        }
        lastPageToLoad = countHelper + START_PAGE
    }
}