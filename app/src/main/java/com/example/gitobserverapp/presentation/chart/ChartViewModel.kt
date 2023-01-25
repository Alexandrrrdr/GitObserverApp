package com.example.gitobserverapp.presentation.chart

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

    private var _stargazersLiveData: MutableLiveData<PresentationStargazersListModel?> = MutableLiveData<PresentationStargazersListModel?>()
    val stargazersLiveData: MutableLiveData<PresentationStargazersListModel?> get() = _stargazersLiveData

    private var searchLiveData = mutableListOf<SearchModel>()

    private var page = 1

    @RequiresApi(Build.VERSION_CODES.O)
    fun getStargazersList() {
        viewModelScope.launch {
            _chartViewState.postValue(ChartViewState.Loading)
            val tmp: PresentationStargazersListModel
            val domainStargazersList = getStargazersUseCase.getData(
                repo_name = searchLiveData[0].repoName,
                owner_login = searchLiveData[0].repoOwnerName,
                page_number = page
            )
            tmp = DomainToPresentationStargazersListMapper().map(domainStargazersList)
//            _stargazersLiveData.postValue(tmp)
            checkLoadedPage(tmp)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkLoadedPage(list: PresentationStargazersListModel) {
            _chartViewState.postValue(ChartViewState.ViewContentMain)
            Log.d("info", "size is ${list.stargazers_list.size}")
//            loadNewPage()

//        if (list.stargazers_list.isNotEmpty()) {
//            loadNewPage()
//        } else {
//
//            compareYearsModel(stargazersLiveData.value!!.stargazers_list)
//        }
        compareYearsModel(list = list.stargazers_list)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadNewPage() {
        page++
        getStargazersList()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun compareYearsModel(list: List<PresentationStargazersListItem>) {

        val tmpMatchedList = mutableListOf<BarChartModel>()
        val endDate = list[list.lastIndex].starred_at.year
        var startDate = list[Constants.ZERO_PAGE].starred_at.year

        while (startDate <= endDate) {
            val tmpUsers = mutableListOf<PresentationStargazersListItem>()
            val list1 = list.filter { it.starred_at.year == startDate }
            for (i in list1.indices) {
                tmpUsers.add(i, list1[i])
            }
            tmpMatchedList.add(
                element = BarChartModel(
                    period = startDate,
                    userInfo = tmpUsers
                )
            )
            startDate++
        }
        setBarChartYearsData(tmpMatchedList)
    }


//    @RequiresApi(Build.VERSION_CODES.O)
//    fun parseChartData(starredDataList: ArrayList<PresentationChartListItem>) {
//        val starParsedList = mutableListOf<PresentationChartListItem>()
//        var starredModel: PresentationChartListItem
//
//        for (i in starredDataList.indices) {
//            starredModel = PresentationChartListItem(
//                starred_at = starredDataList[i].starred_at,
//                id = starredDataList[i].id,
//                login = starredDataList[i].login,
//                avatar_url = starredDataList[i].avatar_url
//            )
//            starParsedList.add(i, starredModel)
//        }
//        compareYearsModel(starParsedList)
//    }



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