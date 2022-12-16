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
import com.example.gitobserverapp.presentation.chart.model.StarParsedModel
import com.example.gitobserverapp.presentation.helper.ViewState
import com.example.gitobserverapp.utils.Constants
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class ChartViewModel : ViewModel() {

    private var _chartLiveData: MutableLiveData<List<ChartModel>> = MutableLiveData<List<ChartModel>>()
    val chartLiveData: LiveData<List<ChartModel>> get() = _chartLiveData

    private var _viewStateLiveData: MutableLiveData<ViewState> = MutableLiveData<ViewState>()
    val viewStateLiveData get() = _viewStateLiveData

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData(starLis: List<StarredModelItem>) {
        val tmpStarList: ArrayList<ChartModel> = arrayListOf()
        var tmpModel: ChartModel?
        for (i in starLis.indices) {
            tmpModel = ChartModel(
                repoOwnerName = starLis[i].user.login,
                starredAt = starLis[i].starred_at
            )
            tmpStarList.add(tmpModel)
        }
        _chartLiveData.postValue(tmpStarList as List<ChartModel>)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getStarInfo(repoOwnerName: String, repoName: String) {

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
                            if (list !=null && list.isNotEmpty()){
                                setData(list)
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

    private var _starredLiveData = MutableLiveData <List<StarParsedModel>>()
    val starredLiveData: LiveData<List<StarParsedModel>> get() = _starredLiveData

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseStarredData(starredDateList: List<ChartModel>) {

        val starParsedList = mutableListOf<StarParsedModel>()

        for (i in starredDateList.indices) {
            println(starredDateList[i].starredAt)

//            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-d'T'hh:mm:ss.SSS'Z'")
//            val localDate = LocalDate.parse(starredDateList[i].starredAt, inputFormatter)
//            starredModel = StarParsedModel(starred_at = localDate, repoOwnerName = starredDateList[i].repoOwnerName)
//            starParsedList.add(i, starredModel)

//            val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy", Locale.ENGLISH)
//            val starDate = LocalDate.parse("2018-04-10T04:00:00.000Z", inputFormatter)
//            println(starredDateList.size.toString())
//            var localDate = LocalDate.parse(starredDateList[i].starredAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
//            println(starredDateList[i].starredAt)
        }

        _starredLiveData.postValue(starParsedList)
        }
}