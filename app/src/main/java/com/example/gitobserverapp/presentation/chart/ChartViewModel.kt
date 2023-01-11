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
import com.example.gitobserverapp.utils.Constants
import com.example.gitobserverapp.utils.parse_period.PeriodList
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll
import java.time.*
import java.util.Comparator
import javax.inject.Inject

class ChartViewModel @Inject constructor(private val apiRepository: ApiRepository) : ViewModel() {

    private var _starredUsersLiveData = MutableLiveData<List<UserModel>>()
    val starredUsersLiveData: LiveData<List<UserModel>> get() = _starredUsersLiveData

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
    fun getDataFromGitHub(repoOwnerName: String, repoName: String, createdAt: String, page: Int) {
        _chartScreenState.postValue(ChartViewState.Loading)
        viewModelScope.launch {
            try {
                val retroRequest = apiRepository.getStarredData(login = repoOwnerName, repoName = repoName, page = page)
                if (retroRequest.isSuccessful && retroRequest.body() != null) {
                    when (retroRequest.code()) {
                        200 -> {
                            retroRequest.body().let { list ->
                                if (list != null && list.isNotEmpty()) {
                                    parseChartData(
                                        starredDataList = list,
                                        created = createdAt,
                                        repoName = repoName
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
        _starredUsersLiveData.postValue(starParsedList)
        compareYearsModel(starParsedList)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun compareYearsModel(list: List<UserModel>) {

        val tmpMatchedList = mutableListOf<BarChartModel>()
        val tmpUsers = mutableListOf<User>()
        val findMinStarredDate = list.minWith(Comparator.comparingInt { it.createdAt.year })
        val todayDate = LocalDate.now().year
        var startDate = findMinStarredDate.starredAt.year

        while (startDate <= todayDate) {
            val (match, _) = list.partition { it.starredAt.year == startDate }
            for (i in match.indices) {
                tmpUsers.add(match[i].user)
            }
            tmpMatchedList.add(
                element = BarChartModel(
                    period = startDate,
                    amount = match.size,
                    userInfo = tmpUsers
                )
            )
            tmpUsers.clear()
            startDate++
        }
        setBarChartYearsData(tmpMatchedList)
    }

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