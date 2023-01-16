package com.example.gitobserverapp.presentation.chart

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitobserverapp.data.network.model.starred.StarredModelItem
import com.example.gitobserverapp.data.network.model.starred.User
import com.example.gitobserverapp.data.repository.ApiRepository
import com.example.gitobserverapp.presentation.chart.chart_helper.ChartViewState
import com.example.gitobserverapp.presentation.chart.model.UserModel
import com.example.gitobserverapp.presentation.chart.model.BarChartModel
import com.example.gitobserverapp.presentation.chart.model.RadioButtonModel
import com.example.gitobserverapp.presentation.chart.model.SearchModel
import com.example.gitobserverapp.utils.Constants
import kotlinx.coroutines.launch
import java.time.*
import javax.inject.Inject

class ChartViewModel @Inject constructor(private val apiRepository: ApiRepository) : ViewModel() {

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

    private var searchValues = mutableListOf<SearchModel>()

    fun setSearchValues(repoOwnerName: String, repoName: String, createdAt: String, page: Int){
        searchValues.add(0, SearchModel(repoOwnerName = repoOwnerName, repoName = repoName, createdAt = createdAt, page = page))
    }

    init {
        _chartPageObserveLiveData.value = 1
    }

    fun setNetworkStatus(value: Boolean){
        _chartNetworkLiveData.postValue(value)
    }

    fun setPageObserverLiveData(value: Int){
        _chartPageObserveLiveData.postValue(+value)
    }

    fun setScreenState(chartViewState: ChartViewState){
        _chartScreenState.postValue(chartViewState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDataFromGitHub(page: Int) {
        _chartScreenState.postValue(ChartViewState.Loading)

        viewModelScope.launch {
            try {
                val retroRequest = apiRepository.getStarredData(
                    login = searchValues[Constants.DEF_ZERO_PAGE].repoOwnerName,
                    repoName = searchValues[Constants.DEF_ZERO_PAGE].repoName,
                    page = searchValues[Constants.DEF_ZERO_PAGE].page
                )
                if (retroRequest.isSuccessful && retroRequest.body() != null) {
                    when (retroRequest.code()) {
                        200 -> {
                            retroRequest.body().let { list ->
                                if (list != null && list.isNotEmpty()) {
                                    parseChartData(
                                        starredDataList = list,
                                        created = searchValues[Constants.DEF_ZERO_PAGE].createdAt,
                                        repoName = searchValues[Constants.DEF_ZERO_PAGE].repoName
                                    )
                                } else {
                                    _chartScreenState.postValue(ChartViewState.Error("Check your request details"))
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception){
                _chartNetworkLiveData.postValue(false)
            }
        }
    }

    //Parse date format to localDate
    @RequiresApi(Build.VERSION_CODES.O)
    fun parseChartData(starredDataList: List<StarredModelItem>, created: String, repoName: String) {
        val starParsedList = mutableListOf<UserModel>()
        var starredModel: UserModel
        val date = dateConverter(created)

        for (i in starredDataList.indices) {
            val localDate = dateConverter(starredDataList[i].starred_at)
            starredModel = UserModel(
                user = starredDataList[i].user,
                starredAt = localDate,
                createdAt = date,
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
        val endDate = list[list.size-1].starredAt.year
        var startDate = list[0].starredAt.year

        while (startDate <= endDate){
            val tmpUsers = mutableListOf<User>()
                val list1 = list.filter { it.starredAt.year == startDate }
                for (i in list1.indices){
                    tmpUsers.add(i, list1[i].user)
                }
                tmpMatchedList.add(element = BarChartModel(
                    period = startDate,
                    userInfo = tmpUsers
                ))
            startDate++
        }
        setBarChartYearsData(tmpMatchedList)
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun checkLoadNewPage(list: List<StarredModelItem>){
//        val startDate = dateConverter(list[0].starred_at)
//        val endDate = dateConverter(list[list.size-1].starred_at)
//        if (endDate == startDate){
//            getDataFromGitHub()
//        }
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateConverter(value: String): LocalDate {
        val instant = Instant.parse(value)
        return LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.id)).toLocalDate()
    }

    fun setCheckedRadioButton(radioId: Int){
        _radioButtonCheckedLiveData.postValue(RadioButtonModel(radioId))
    }

    private fun setBarChartYearsData(list: List<BarChartModel>){
        _barChartListLiveData.postValue(list)
    }
}