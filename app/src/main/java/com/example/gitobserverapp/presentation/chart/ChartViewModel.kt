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
import com.example.gitobserverapp.presentation.chart.model.ChartModel
import com.example.gitobserverapp.presentation.chart.model.ComparedModel
import com.example.gitobserverapp.presentation.chart.model.RadioButtonModel
import com.example.gitobserverapp.utils.Constants
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch
import java.time.*

class ChartViewModel : ViewModel() {

    private var _starredUsersLiveData = MutableLiveData<List<ChartModel>>()
    val starredUsersLiveData: LiveData<List<ChartModel>> get() = _starredUsersLiveData

    private var _barChartYearsLiveData = MutableLiveData<List<ComparedModel>>()
    val barChartYearsLiveData: LiveData<List<ComparedModel>> get() = _barChartYearsLiveData

    private var _radioCheckedLiveData = MutableLiveData<RadioButtonModel>()
    val radioCheckedLiveData: LiveData<RadioButtonModel> get() = _radioCheckedLiveData


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
        val starParsedList = mutableListOf<ChartModel>()
        var starredModel: ChartModel
        val date = dateConverter(created)

        for (i in starredDateList.indices) {

            val localDate = dateConverter(starredDateList[i].starred_at)
            starredModel = ChartModel(
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateComparator(list: List<ChartModel>, keyValue: Int) {

        val startDate = list[0].createdAt
        val currentDate = LocalDate.now()
        var counter = 0
        val compareList = arrayListOf<ComparedModel>()

        when (keyValue) {
            //years
            0 -> {
                for (i in startDate.year..currentDate.year) {
                    for (y in list.indices) {
                        if (i == list[y].starredAt.year) {
                            counter++
                            compareList.add(
                                ComparedModel(
                                    item = i,
                                    amount = counter,
                                    userInfo = list[y].users
                                )
                            )
                        }
                    }
                }
                Log.d("chart", "${compareList.size}")
                _barChartYearsLiveData.postValue(compareList)
            }
            1 -> {

            }
            2 -> {

            }
        }
    }

    private val yearsData = mutableListOf<BarEntry>()
    private val _barDataSet = MutableLiveData(BarDataSet(yearsData, "Years"))
    val barDataSet: LiveData<BarDataSet> get() = _barDataSet

    fun initBarChart(list: List<ComparedModel>){
        for (i in list.indices){
            yearsData.add(BarEntry(list[i].item.toFloat(), list[i].amount.toFloat()))
        }
        _barDataSet.value = BarDataSet(yearsData, "Years")

    }
}