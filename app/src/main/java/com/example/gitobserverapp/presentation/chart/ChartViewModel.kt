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
import com.example.gitobserverapp.presentation.chart.model.ComparedList
import com.example.gitobserverapp.utils.Constants
import kotlinx.coroutines.launch
import java.time.*

class ChartViewModel : ViewModel() {

    private var _starredUsersLiveData = MutableLiveData<List<ChartModel>>()
    val starredUsersLiveData: LiveData<List<ChartModel>> get() = _starredUsersLiveData

//    private var _barChartLiveData = MutableLiveData<List<ComparedList>>()
//    val barChartLiveData: LiveData<List<ComparedList>> get() = _barChartLiveData

    @RequiresApi(Build.VERSION_CODES.O)
    fun getStarInfo(repoOwnerName: String, repoName: String, createdAt: String) {

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
                                parseChartData(list, createdAt)
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
    fun parseChartData(starredDateList: List<StarredModelItem>, created: String) {

        val starParsedList = mutableListOf<ChartModel>()
        var starredModel: ChartModel
        val date = dateConverter(created)

        for (i in starredDateList.indices) {

            val localDate = dateConverter(starredDateList[i].starred_at)
            starredModel = ChartModel(
                users = starredDateList[i].user,
                starredAt = localDate,
                createdAt = date
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateComparator(list: List<ChartModel>, searchValue: LocalDate, key: Int): Int{

        val startDate = list[0].createdAt
        val currentDate = LocalDate.now()
        var counter = 0
        val compareList = arrayListOf<ComparedList>()

        when(key){
            //years
            0 -> {
                for (i in list.indices){
                    if (list[i].starredAt.year == list[i+1].starredAt.year){

                    }
                }
            }
            1 -> {

            }
            2 -> {

            }
        }
        return 0
    }
}