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

    private var _stargazersLiveData: MutableLiveData<PresentationStargazersListModel?> = MutableLiveData<PresentationStargazersListModel?>()
    val stargazersLiveData: MutableLiveData<PresentationStargazersListModel?> get() = _stargazersLiveData

    private var searchLiveData = mutableListOf<SearchModel>()

    private var page = 1

    @RequiresApi(Build.VERSION_CODES.O)
    fun getStargazersList() {
        viewModelScope.launch {
            var tmp: PresentationStargazersListModel? = null
            val domainStargazersList = getStargazersUseCase.getData(
                value_one = searchLiveData[0].repoName,
                value_two = searchLiveData[0].repoOwnerName,
                value_three = page
            )
            tmp = DomainToPresentationStargazersListMapper().map(domainStargazersList)
            _stargazersLiveData.postValue(tmp)
//            checkLoadedPage(tmp)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkLoadedPage(list: PresentationStargazersListModel) {

        _chartViewState.postValue(ChartViewState.Loading)
        if (list.stargazers_list.isNotEmpty()) {
            loadNewPage()
        } else {
//            compareYearsModel(stargazersLiveData.value!!.stargazers_list)
//            parseChartData(list.stargazers_list)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadNewPage() {
        page++
        getStargazersList()
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

    fun setNetworkStatus(value: Boolean) {
        _chartNetworkLiveData.postValue(value)
    }

    fun setPageObserverLiveData(value: Int) {
        _chartPageObserveLiveData.postValue(+value)
    }

    fun setScreenState(chartViewState: ChartViewState) {
        _chartViewState.postValue(chartViewState)
    }

    fun setCheckedRadioButton(radioId: Int) {
        _radioButtonCheckedLiveData.postValue(RadioButtonModel(radioId))
    }

    fun setBarChartYearsData(list: List<BarChartModel>) {
        _barChartListLiveData.postValue(list)
    }
}