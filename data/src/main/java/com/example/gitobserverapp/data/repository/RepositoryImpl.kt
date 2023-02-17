package com.example.gitobserverapp.data.repository

import android.util.Log
import com.example.gitobserverapp.data.remote.GitRetrofitService
import com.example.gitobserverapp.data.remote.model.RemoteRepo
import com.example.gitobserverapp.data.remote.model.RemoteStarDate
import com.example.gitobserverapp.data.utils.Constants
import com.example.gitobserverapp.data.utils.Constants.MAX_PER_PAGE
import com.example.gitobserverapp.data.utils.Constants.START_PAGE
import com.example.gitobserverapp.domain.model.NetworkState
import com.example.gitobserverapp.domain.repository.GetRepository
import okio.IOException
import retrofit2.Response
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val gitRetrofitService: GitRetrofitService
) : GetRepository {

    override suspend fun getRepos(
        userName: String
    ): NetworkState<List<RemoteRepo>> {
        return try {
            val getRepos = gitRetrofitService.getOwnerRepos(
                userName = userName,
                page = START_PAGE,
            )
            if (getRepos.isSuccessful){
                if (getRepos.body() != null){
                    NetworkState.Success(getRepos.body()!!)
                }
                else {
                    NetworkState.InvalidData
                }
            } else {
                when(getRepos.code()){
                    301 -> NetworkState.HttpErrors.ResourceRemoved(getRepos.message())
                    302 -> NetworkState.HttpErrors.RemovedResourceFound(getRepos.message())
                    403 -> NetworkState.HttpErrors.ResourceForbidden(getRepos.message())
                    404 -> NetworkState.HttpErrors.ResourceNotFound(getRepos.message())
                    500 -> NetworkState.HttpErrors.InternalServerError(getRepos.message())
                    502 -> NetworkState.HttpErrors.BadGateWay(getRepos.message())
                    else -> NetworkState.Error(getRepos.message())
                }
            }
        } catch (e: IOException){
            NetworkState.NetworkException(e.cause.toString())
        }
    }

    override suspend fun getStarGroup(
        repoName: String,
        ownerName: String,
        pageNumber: Int
    ): NetworkState<List<RemoteStarDate>> {
        val tmpReadyList = mutableListOf<RemoteStarDate>()
        return try {

            Log.d("info", "RepoName - $repoName, ownerName - $ownerName, pageNumber - $pageNumber")
            var tmp = gitRetrofitService.getStarUsers(
                repoName = repoName,
                ownerLogin = ownerName,
                perPage = MAX_PER_PAGE,
                page = pageNumber
            )

            if (tmp.isSuccessful) {
                var tmpPage = 2
                while (tmp.body()?.isNotEmpty() == true) {
                    tmpReadyList.addAll(tmp.body()!!)
                    tmp = loadPageAndNext(gitRetrofitService, repoName, ownerName, tmpPage)
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
    ): Response<List<RemoteStarDate>> {
        return gitRetrofitService.getStarUsers(
            repoName = repo_name,
            ownerLogin = owner_login,
            perPage = MAX_PER_PAGE,
            page = page_number
        )
    }
}