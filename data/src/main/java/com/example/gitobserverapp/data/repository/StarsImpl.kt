package com.example.gitobserverapp.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.gitobserverapp.data.remote.GitRetrofitService
import com.example.gitobserverapp.data.remote.model.RemoteStarGroup
import com.example.gitobserverapp.data.utils.Constants
import com.example.gitobserverapp.data.utils.mapper.StarGroupToDomain
import com.example.gitobserverapp.domain.model.NetworkState
import com.example.gitobserverapp.domain.model.StarGroup
import com.example.gitobserverapp.domain.repository.GetStars
import okio.IOException
import retrofit2.Response
import javax.inject.Inject

class StarsImpl
@Inject constructor(private val gitRetrofitService: GitRetrofitService,
private val starGroupToDomain: StarGroupToDomain): GetStars{

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getData(
        repo_name: String,
        owner_login: String,
        page_number: Int
    ): NetworkState<List<StarGroup>> {
        val tmpReadyList = mutableListOf<StarGroup>()
        return try {

            var tmp = gitRetrofitService.getStarredData(
                repo_name = repo_name,
                owner_login = owner_login,
                per_page = Constants.MAX_PER_PAGE,
                page = page_number
            )

            if (tmp.isSuccessful) {
                var tmpPage = 2
                while (tmp.body()?.isNotEmpty() == true) {
                    tmpReadyList.addAll(starGroupToDomain.mapListStarGroup(tmp.body()!!))
                    tmp = loadPageAndNext(gitRetrofitService, repo_name, owner_login, tmpPage)
                    tmpPage++

                }
                NetworkState.Success(tmpReadyList)
            } else {
                when(tmp.code()){
                    301 -> NetworkState.HttpErrors.ResourceRemoved(tmp.message())
                    302 -> NetworkState.HttpErrors.RemovedResourceFound(tmp.message())
                    403 -> NetworkState.HttpErrors.ResourceForbidden(tmp.message())
                    404 -> NetworkState.HttpErrors.ResourceNotFound(tmp.message())
                    500 -> NetworkState.HttpErrors.InternalServerError(tmp.message())
                    502 -> NetworkState.HttpErrors.BadGateWay(tmp.message())
                    else -> NetworkState.Error(tmp.message())
                }
            }
        } catch (e: IOException) {
            NetworkState.NetworkException(e.message.toString())
        }

    }
    private suspend fun loadPageAndNext(
        gitRetrofitService: GitRetrofitService,
        repo_name: String,
        owner_login: String,
        page_number: Int
    ): Response<List<RemoteStarGroup>> {
        return gitRetrofitService.getStarredData(
            repo_name = repo_name,
            owner_login = owner_login,
            per_page = Constants.MAX_PER_PAGE,
            page = page_number
        )
    }
}


