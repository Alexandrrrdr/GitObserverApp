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
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

class ChartViewModel : ViewModel() {

    private var _chartLiveData: MutableLiveData<List<ChartModel>> = MutableLiveData<List<ChartModel>>()
    val chartLiveData: LiveData<List<ChartModel>> get() = _chartLiveData

    private var _viewStateLiveData: MutableLiveData<ViewState> = MutableLiveData<ViewState>()
    val viewStateLiveData get() = _viewStateLiveData

    private var _starredLiveData = MutableLiveData<List<StarParsedModel>>()
    val starredLiveData: LiveData<List<StarParsedModel>> get() = _starredLiveData

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
                            if (list != null && list.isNotEmpty()) {
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseStarredData(starredDateList: List<ChartModel>) {

        val starParsedList = mutableListOf<StarParsedModel>()
        var starredModel: StarParsedModel

        for (i in starredDateList.indices) {
            val instance = Instant.parse(starredDateList[i].starredAt)
            val result = LocalDateTime.ofInstant(instance, ZoneId.of(ZoneOffset.UTC.id))

            starredModel = StarParsedModel(
                starred_at = result.toLocalDate(),
                repoOwnerName = starredDateList[i].repoOwnerName
            )

            println(starredModel.starred_at.toString())
            starParsedList.add(i, starredModel)
        }
        _starredLiveData.postValue(starParsedList)
    }
}