package com.example.gitobserverapp.presentation.chart

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitobserverapp.data.network.RetrofitInstance
import com.example.gitobserverapp.data.network.model.starred.StarredModelItem
import com.example.gitobserverapp.presentation.chart.model.UserModel
import com.example.gitobserverapp.presentation.chart.model.BarChartModel
import com.example.gitobserverapp.presentation.chart.model.RadioButtonModel
import com.example.gitobserverapp.utils.Constants
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.launch
import java.time.*

class ChartViewModel : ViewModel() {

    private var _starredUsersLiveData = MutableLiveData<List<UserModel>>()
    val starredUsersLiveData: LiveData<List<UserModel>> get() = _starredUsersLiveData

    private var _barChartYearsLiveData = MutableLiveData<List<BarChartModel>>()
    val barChartYearsLiveData: LiveData<List<BarChartModel>> get() = _barChartYearsLiveData

    private var _barChartMonthsLiveData = MutableLiveData<List<BarChartModel>>()
    val barChartMonthsLiveData: LiveData<List<BarChartModel>> get() = _barChartMonthsLiveData

    private var _barChartWeeksLiveData = MutableLiveData<List<BarChartModel>>()
    val barChartWeeksLiveData: LiveData<List<BarChartModel>> get() = _barChartWeeksLiveData

    private var _radioCheckedLiveData = MutableLiveData<RadioButtonModel>()
    val radioCheckedLiveData: LiveData<RadioButtonModel> get() = _radioCheckedLiveData

    private val _barDataSet = MutableLiveData<BarDataSet>()
    val barDataSet: LiveData<BarDataSet> get() = _barDataSet

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDataFromGitHub(repoOwnerName: String, repoName: String, createdAt: String) {

        viewModelScope.launch {
            val retroRequest = RetrofitInstance.retrofitInstance.getStarredData(
                owner_login = repoOwnerName,
                repo_name = repoName,
                per_page = Constants.PER_PAGE
            )
            if (retroRequest.isSuccessful && retroRequest.body() != null) {
                when (retroRequest.code()) {
                    in 200..421 -> {
                        retroRequest.body().let { list ->
                            if (list != null && list.isNotEmpty()) {
                                parseChartData(
                                    starredDateList = list,
                                    created = createdAt,
                                    repoName = repoName
                                )
                            } else {
                                Log.d("charts", "No data to show you")
                            }
                        }
                    }
                    in 422..500 -> {
                        Log.d("charts", "${retroRequest.code()}")
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
        _radioCheckedLiveData.postValue(RadioButtonModel(radioId))
    }

    fun setBarChartYearsData(list: List<BarChartModel>){
        _barChartYearsLiveData.value = list
    }



    fun setBarChartData(years: List<BarEntry>){
        _barDataSet.value = BarDataSet(years, "Years")
    }
}