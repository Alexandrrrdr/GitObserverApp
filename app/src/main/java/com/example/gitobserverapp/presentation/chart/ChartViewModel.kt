package com.example.gitobserverapp.presentation.chart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitobserverapp.data.network.RetrofitInstance
import com.example.gitobserverapp.data.network.model.starred.StarredModelItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChartViewModel : ViewModel() {

    private var _chartData: MutableLiveData<List<ChartModel>> = MutableLiveData<List<ChartModel>>()
    val chartData: LiveData<List<ChartModel>> get() = _chartData

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
        _chartData.postValue(tmpStarList as List<ChartModel>)
    }

    fun getStarGazersData(repoOwnerName: String, repoName: String) {
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

    private suspend fun parseStarredData(starredDateList: List<ChartModel>){
        val tmpList: ArrayList<String> = arrayListOf()
        val starParsedList: ArrayList<StarParsedModel>? = null
        val starModel: StarParsedModel? = null
        val delimeterDephise = "-"
        val delimeterLetter = "T"
        for (i in starredDateList.indices){
            tmpList.addAll(starredDateList[i].starredAt.split(delimeterDephise, delimeterLetter, ignoreCase = true))
            starModel = StarParsedModel(year = tmpList[i].toInt(), )
        }
    }

}