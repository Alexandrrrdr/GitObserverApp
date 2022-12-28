package com.example.gitobserverapp.presentation.chart

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitobserverapp.data.network.model.starred.StarredModelItem
import com.example.gitobserverapp.data.repository.ApiRepository
import com.example.gitobserverapp.presentation.chart.chart_helper.ChartState
import com.example.gitobserverapp.presentation.chart.model.UserModel
import com.example.gitobserverapp.presentation.chart.model.BarChartModel
import com.example.gitobserverapp.presentation.chart.model.RadioButtonModel
import com.github.mikephil.charting.data.BarDataSet
import kotlinx.coroutines.launch
import java.time.*
import javax.inject.Inject

class ChartViewModel @Inject constructor(private val apiRepository: ApiRepository) : ViewModel() {

    private var _starredUsersLiveData = MutableLiveData<List<UserModel>>()
    val starredUsersLiveData: LiveData<List<UserModel>> get() = _starredUsersLiveData

    private var _barChartListLiveData = MutableLiveData<List<BarChartModel>>()
    val barChartListLiveData: LiveData<List<BarChartModel>> get() = _barChartListLiveData

    private var _radioButtonCheckedLiveData = MutableLiveData<RadioButtonModel>()
    val radioButtonCheckedLiveData: LiveData<RadioButtonModel> get() = _radioButtonCheckedLiveData

    private var _chartScreenState = MutableLiveData<ChartState>()
    val chartScreenState: LiveData<ChartState> get() = _chartScreenState

    private val _barDataSet = MutableLiveData<BarDataSet>()
    val barDataSet: LiveData<BarDataSet> get() = _barDataSet

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDataFromGitHub(repoOwnerName: String, repoName: String, createdAt: String) {
        viewModelScope.launch {
            _chartScreenState.postValue(ChartState.Loading)
            val retroRequest = apiRepository.getStarredData(login = repoOwnerName, repoName = repoName)
            if (retroRequest.isSuccessful && retroRequest.body() != null) {
                when (retroRequest.code()) {
                    in 200..421 -> {
                        retroRequest.body().let { list ->
                            if (list != null && list.isNotEmpty()) {
                                _chartScreenState.postValue(ChartState.ViewContentMain(list))
                                parseChartData(
                                    starredDateList = list,
                                    created = createdAt,
                                    repoName = repoName
                                )
                            } else {
                                _chartScreenState.postValue(ChartState.Error("No data to show you"))
                            }
                        }
                    }
                    in 422..500 -> {
                        _chartScreenState.postValue(ChartState.Error("No server response"))
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseChartData(starredDateList: List<StarredModelItem>, created: String, repoName: String) {
        val starParsedList = mutableListOf<UserModel>()
        var starredModel: UserModel
        val date = dateConverter(created)

        for (i in starredDateList.indices) {

            val localDate = dateConverter(starredDateList[i].starred_at)
            starredModel = UserModel(
                users = starredDateList[i].user,
                starredAt = localDate,
                createdAt = date,
                repoName = repoName
            )
            starParsedList.add(i, starredModel)
        }
        _starredUsersLiveData.postValue(starParsedList)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateConverter(value: String): LocalDate {
        val instant = Instant.parse(value)
        return LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.id)).toLocalDate()
    }

    fun setCheckedRadioButton(radioId: Int){
        _radioButtonCheckedLiveData.postValue(RadioButtonModel(radioId))
    }

    fun setBarChartYearsData(list: List<BarChartModel>){
        _barChartListLiveData.postValue(list)
    }

    fun setErrorState(error: String?){
        _chartScreenState.postValue(error?.let { ChartState.Error(error = it) })
    }
}