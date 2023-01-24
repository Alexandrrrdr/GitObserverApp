package com.example.gitobserverapp.presentation.chart

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitobserverapp.domain.usecase.GetStargazersUseCase
import com.example.gitobserverapp.presentation.chart.chart_helper.ChartViewState
import com.example.gitobserverapp.presentation.chart.model.*
import com.example.gitobserverapp.presentation.mapping.stargazers.DomainToPresentationStargazersListMapper
import kotlinx.coroutines.launch

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

    private var _stargazersLiveData: MutableLiveData<PresentationChartListModel> = MutableLiveData<PresentationChartListModel>()
    val stargazersLiveData: LiveData<PresentationChartListModel> get() = _stargazersLiveData

    private var searchLiveData = mutableListOf<SearchModel>()

    private var page = 1

    @RequiresApi(Build.VERSION_CODES.O)
    fun getStargazersList() {
        viewModelScope.launch {
            val domainStargazersList = getStargazersUseCase.getData(
                value_one = searchLiveData[0].repoOwnerName,
                value_two = searchLiveData[0].repoName,
                value_three = page
            )
            val tmp = DomainToPresentationStargazersListMapper().map(domainStargazersList)
//            _stargazersLiveData.postValue(tmp)
            checkLoadedPage(tmp)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkLoadedPage(list: PresentationChartListModel) {
        if (list.stargazers_list.isNotEmpty()) {
            loadNewPage()
        } else {
//            parseChartData(list.stargazers_list, searchLiveData[0].repoName)
        }
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

    init {
        _chartPageObserveLiveData.value = 1
    }

    fun setNetworkStatus(value: Boolean) {
        _chartNetworkLiveData.postValue(value)
    }

    fun setPageObserverLiveData(value: Int) {
        _chartPageObserveLiveData.postValue(+value)
    }

    fun setScreenState(chartViewState: ChartViewState) {
        _chartViewState.postValue(chartViewState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadNewPage() {
        page++
        getStargazersList()
    }

    //Parse date format from String to LocalDate
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun parseChartData(starredDataList: ArrayList<PresentationChartListItem>, repoName: String) {
//        val starParsedList = mutableListOf<PresentationChartListItem>()
//        var starredModel: PresentationChartListItem
//
//        for (i in starredDataList.indices) {
//            val localDate = dateConverter(starredDataList[i].starred_at)
//            starredModel = PresentationChartListItem(
//                starred_at = localDate,
//                id = starredDataList[i].id,
//                login = starredDataList[i].login,
//                avatar_url = starredDataList[i].avatar_url
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
//        var startDate = list[ZERO_PAGE].starredAt.year
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



    fun setCheckedRadioButton(radioId: Int) {
        _radioButtonCheckedLiveData.postValue(RadioButtonModel(radioId))
    }

    private fun setBarChartYearsData(list: List<BarChartModel>) {
        _barChartListLiveData.postValue(list)
    }
}