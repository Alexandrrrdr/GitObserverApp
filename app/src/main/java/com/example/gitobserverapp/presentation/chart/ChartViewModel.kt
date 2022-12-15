package com.example.gitobserverapp.presentation.chart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitobserverapp.data.network.RetrofitInstance
import com.example.gitobserverapp.data.network.model.starred.StarredModelItem
import com.example.gitobserverapp.presentation.chart.model.ChartModel
import com.example.gitobserverapp.presentation.chart.model.StarParsedModel
import com.example.gitobserverapp.presentation.chart.model.StarredParsedToDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class ChartViewModel : ViewModel() {

    private var _chartLiveData: MutableLiveData<List<ChartModel>> = MutableLiveData<List<ChartModel>>()
    val chartLiveData: LiveData<List<ChartModel>> get() = _chartLiveData

    private fun setData(starLis: List<StarredModelItem>) {
        val tmpStarList: ArrayList<ChartModel> = arrayListOf()
        var tmpModel: ChartModel? = null
        for (i in starLis.indices) {
            tmpModel = ChartModel(
                repoOwnerName = starLis[i].user.login,
                starredAt = starLis[i].starred_at
            )
            tmpStarList.add(tmpModel)
        }
        _chartLiveData.postValue(tmpStarList as List<ChartModel>)
    }

    fun getStarInfo(repoOwnerName: String, repoName: String) {
        val retroRequest = RetrofitInstance.retrofitInstance.getStarredData(
            owner_login = repoOwnerName,
            repo_name = repoName
        )
        retroRequest.enqueue(object : Callback<List<StarredModelItem>> {
            override fun onResponse(
                call: Call<List<StarredModelItem>>,
                response: Response<List<StarredModelItem>>
            ) {
                when (response.code()) {
                    in 200..421 -> {
                        response.body().let { response ->
                            if (response!=null && response.isNotEmpty()){
                                setData(response)
                            } else {
                                Log.d("charts", "No data to show you")
                            }
                        }
                    }
                    in 422..500 -> {
                        Log.d("charts", "${response.code()}")
                    }
                }
            }

            override fun onFailure(call: Call<List<StarredModelItem>>, t: Throwable) {
                Log.d("charts", "${t.message}")
            }
        })
    }

    private var _starParsedLiveData = MutableLiveData<List<StarParsedModel>>()
    val starParsedLiveData get() = _starParsedLiveData

    private fun parseStarredData(starredDateList: List<ChartModel>){
        val tmpList: ArrayList<String> = arrayListOf()
        val starParsedList: ArrayList<StarParsedModel>? = null
        var starModel: StarParsedModel? = null
        val delimeterDephise = "-"
        val delimeterLetter = "T"
        for (i in starredDateList.indices){
            tmpList.addAll(starredDateList[i].starredAt.split(delimeterDephise, delimeterLetter, ignoreCase = true))
            starModel = StarParsedModel(year = tmpList[0].toInt(), month = tmpList[1].toInt(), day = tmpList[2].toInt(), repoOwnerName = starredDateList[i].repoOwnerName)
            starParsedList?.add(starModel)
//            starModel = StarredParsedToDate()
        }
        _starParsedLiveData.postValue(starParsedList!!)
    }

}