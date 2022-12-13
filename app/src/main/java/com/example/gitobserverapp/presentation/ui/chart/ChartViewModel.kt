package com.example.gitobserverapp.presentation.ui.chart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitobserverapp.data.repository.network.RetrofitInstance
import com.example.gitobserverapp.data.repository.network.model.starred.StarredModel
import com.example.gitobserverapp.data.repository.network.model.starred.StarredModelItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ChartViewModel : ViewModel() {

    private var _chartData: MutableLiveData<List<ChartModel>> = MutableLiveData<List<ChartModel>>()
    val charData: LiveData<List<ChartModel>> get() = _chartData

    private fun setData(starLis: ArrayList<StarredModelItem>) {
        val tmpRepos: ArrayList<ChartModel> = arrayListOf()
        var tmpModel: ChartModel? = null
        for (i in 0..starLis.size) {
            tmpModel = ChartModel(
                repoOwnerName = starLis[i].user.login,
                starredAt = starLis[i].starred_at
            )
            tmpRepos.add(tmpModel)
        }
        _chartData.postValue(tmpRepos)
    }

    fun getStarGazersData(repoOwnerName: String, repoName: String) {
        val retroRequest = RetrofitInstance.retrofitInstance.getStarredData(
            owner_login = repoOwnerName,
            repo_name = repoName
        )
        retroRequest.enqueue(object : Callback<ArrayList<StarredModelItem>> {
            override fun onResponse(
                call: Call<ArrayList<StarredModelItem>>,
                response: Response<ArrayList<StarredModelItem>>
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

            override fun onFailure(call: Call<ArrayList<StarredModelItem>>, t: Throwable) {
                Log.d("charts", "${t.message}")
            }

        })
    }
}