package com.example.gitobserverapp.presentation.chart

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitobserverapp.data.network.model.starred.StarredModelItem
import com.example.gitobserverapp.data.network.model.starred.User
import com.example.gitobserverapp.data.repository.StarRepository
import com.example.gitobserverapp.presentation.chart.chart_helper.ChartViewState
import com.example.gitobserverapp.presentation.chart.model.BarChartModel
import com.example.gitobserverapp.presentation.chart.model.RadioButtonModel
import com.example.gitobserverapp.presentation.chart.model.SearchModel
import com.example.gitobserverapp.presentation.chart.model.UserModel
import com.example.gitobserverapp.utils.Constants.ZERO_PAGE
import kotlinx.coroutines.launch
import java.time.*
import javax.inject.Inject

class ChartViewModel @Inject constructor(private val starRepository: StarRepository) : ViewModel() {

    private var _barChartListLiveData = MutableLiveData<List<BarChartModel>>()
    val barChartListLiveData: LiveData<List<BarChartModel>> get() = _barChartListLiveData

    private var _radioButtonCheckedLiveData = MutableLiveData<RadioButtonModel>()
    val radioButtonCheckedLiveData: LiveData<RadioButtonModel> get() = _radioButtonCheckedLiveData

    private var _chartScreenState = MutableLiveData<ChartViewState>()
    val chartScreenState: LiveData<ChartViewState> get() = _chartScreenState

    private var _chartNetworkLiveData = MutableLiveData<Boolean>()
    val chartNetworkLiveData: LiveData<Boolean> get() = _chartNetworkLiveData

    private var _chartPageObserveLiveData = MutableLiveData<Int>()
    val chartPageObserveLiveData: LiveData<Int> get() = _chartPageObserveLiveData

    private var searchLiveData = mutableListOf<SearchModel>()

    private val requestBodyList = mutableListOf<StarredModelItem>()

    private var page = 1

    fun clearRequestBodyList() {
        requestBodyList.clear()
    }

    fun setSearchLiveData(repoOwnerName: String, repoName: String, createdAt: String, page: Int) {
        searchLiveData.add(
            0,
            SearchModel(
                repoOwnerName = repoOwnerName,
                repoName = repoName,
                page = page
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
        _chartScreenState.postValue(chartViewState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDataFromGitHub(page: Int) {
        _chartScreenState.postValue(ChartViewState.Loading)
        viewModelScope.launch {
            try {
                val retroRequest = starRepository.getStarredData(
                    login = searchLiveData[ZERO_PAGE].repoOwnerName,
                    repoName = searchLiveData[ZERO_PAGE].repoName,
                    page = page
                )
                if (retroRequest.isSuccessful) {
                    when (retroRequest.code()) {
                        200 -> {
                            retroRequest.body()?.let { checkLoadedPage(it, page) }
                        }
                        422 -> {
                            _chartScreenState.postValue(ChartViewState.Error("Check your request parameters"))
                        }
                    }
                } else {
                    _chartScreenState.postValue(ChartViewState.Error("Error 403 - Requests limit error"))
                }
            } catch (e: Exception) {
                _chartScreenState.postValue(ChartViewState.NetworkError)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkLoadedPage(list: List<StarredModelItem>) {

//        val startDate = dateConverter(requestBodyList[ZERO_PAGE].starred_at)
//        val endDate = dateConverter(requestBodyList[requestBodyList.lastIndex].starred_at)
        if (list.isNotEmpty()) {
            requestBodyList.addAll(list)
            loadNewPage()
        } else {
            parseChartData(requestBodyList, repoName = searchLiveData[ZERO_PAGE].repoName)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadNewPage() {
        page++
        getDataFromGitHub(page = page)
    }

    //Parse date format from String to LocalDate
    @RequiresApi(Build.VERSION_CODES.O)
    fun parseChartData(starredDataList: List<StarredModelItem>, repoName: String) {
        val starParsedList = mutableListOf<UserModel>()
        var starredModel: UserModel

        for (i in starredDataList.indices) {
            val localDate = dateConverter(starredDataList[i].starred_at)
            starredModel = UserModel(
                user = starredDataList[i].user,
                starredAt = localDate,
                repoName = repoName
            )
            starParsedList.add(i, starredModel)
        }
        _chartScreenState.postValue(ChartViewState.ViewContentMain)
        compareYearsModel(starParsedList)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun compareYearsModel(list: List<UserModel>) {

        val tmpMatchedList = mutableListOf<BarChartModel>()
        val endDate = list[list.lastIndex].starredAt.year
        var startDate = list[ZERO_PAGE].starredAt.year

        while (startDate <= endDate) {
            val tmpUsers = mutableListOf<User>()
            val list1 = list.filter { it.starredAt.year == startDate }
            for (i in list1.indices) {
                tmpUsers.add(i, list1[i].user)
            }
            tmpMatchedList.add(
                element = BarChartModel(
                    period = startDate,
                    userInfo = tmpUsers
                )
            )
            startDate++
        }

        tmpMatchedList.addAll(tmpMatchedList.sortedWith(compareByDescending { it.period }))

        for (i in tmpMatchedList.indices){
            Log.d("info", tmpMatchedList[i].period.toString())
        }
        setBarChartYearsData(tmpMatchedList)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateConverter(value: String): LocalDate {
        val instant = Instant.parse(value)
        return LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.id)).toLocalDate()
    }

    fun setCheckedRadioButton(radioId: Int) {
        _radioButtonCheckedLiveData.postValue(RadioButtonModel(radioId))
    }

    private fun setBarChartYearsData(list: List<BarChartModel>) {
        _barChartListLiveData.postValue(list)
    }
}