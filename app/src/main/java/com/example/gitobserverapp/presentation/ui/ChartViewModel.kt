package com.example.gitobserverapp.presentation.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitobserverapp.domain.usecase.GetStargazersUseCase
import com.example.gitobserverapp.presentation.chart.chart_helper.ChartViewState
import com.example.gitobserverapp.presentation.chart.model.*
import com.example.gitobserverapp.presentation.mapping.stargazers.DomainToPresentationStargazersListMapper
import com.example.gitobserverapp.utils.Constants
import kotlinx.coroutines.launch
import java.time.LocalDate

class ChartViewModel(private val getStargazersUseCase: GetStargazersUseCase) : ViewModel() {

    private var _barChartListLiveData = MutableLiveData<List<BarChartModel>>()
    val barChartListLiveData: LiveData<List<BarChartModel>> get() = _barChartListLiveData

    private var _radioButtonCheckedLiveData = MutableLiveData<RadioButtonModel>()
    val radioButtonCheckedLiveData: LiveData<RadioButtonModel> get() = _radioButtonCheckedLiveData

    private var _chartViewState = MutableLiveData<ChartViewState>()
    val chartViewState: LiveData<ChartViewState> get() = _chartViewState

    private var _chartNetworkLiveData = MutableLiveData<Boolean>()
    val chartNetworkLiveData: LiveData<Boolean> get() = _chartNetworkLiveData

    private var _chartPageObserveLiveData = MutableLiveData<Int>()
    val chartPageObserveLiveData: LiveData<Int> get() = _chartPageObserveLiveData

    private var _stargazersLiveData: MutableLiveData<PresentationStargazersListModel> = MutableLiveData<PresentationStargazersListModel>()
    val stargazersLiveData: MutableLiveData<PresentationStargazersListModel> get() = _stargazersLiveData

    private var searchLiveData = mutableListOf<SearchModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getStargazersList(page: Int) {
        viewModelScope.launch {
            _chartViewState.postValue(ChartViewState.Loading)
            val tmpPresentationMapped: PresentationStargazersListModel
            val domainStargazersList = getStargazersUseCase.getData(
                repo_name = searchLiveData[0].repoName,
                owner_login = searchLiveData[0].repoOwnerName,
                page_number = page
            )
            tmpPresentationMapped = DomainToPresentationStargazersListMapper().map(domainStargazersList)
            compareYearsModel(tmpPresentationMapped.stargazers_list)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun compareYearsModel(list: List<PresentationStargazersListItem>) {

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

        setScreenState(ChartViewState.ViewContentMain)
        setBarChartYearsData(matchedListForBarChartModel)
    }

    fun setSearchLiveData(repoOwnerName: String, repoName: String) {
        searchLiveData.add(
            0,
            SearchModel(
                repoOwnerName = repoOwnerName,
                repoName = repoName
            )
        )
    }

    fun setPageObserverLiveData(value: Int) {
        _chartPageObserveLiveData.postValue(+value)
    }

    fun setScreenState(chartViewState: ChartViewState) {
        _chartViewState.postValue(chartViewState)
    }

    private fun setBarChartYearsData(list: List<BarChartModel>) {
        _barChartListLiveData.postValue(list)
    }
}