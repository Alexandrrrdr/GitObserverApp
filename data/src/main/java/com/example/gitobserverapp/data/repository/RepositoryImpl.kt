package com.example.gitobserverapp.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.gitobserverapp.data.remote.GitRetrofitService
import com.example.gitobserverapp.data.remote.model.RemoteRepo
import com.example.gitobserverapp.data.remote.model.RemoteStarDate
import com.example.gitobserverapp.data.utils.Constants.START_PAGE
import com.example.gitobserverapp.domain.model.NetworkState
import com.example.gitobserverapp.domain.repository.GetRepository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val gitRetrofitService: GitRetrofitService
) : GetRepository {

    override suspend fun getRepos(
        userName: String
    ): List<RemoteRepo> {
//        try {
            val repos = gitRetrofitService.getOwnerRepos(
                userName = userName,
                page = START_PAGE,
            )
//            if (repos.isSuccessful && repos.body()!= null) {
                return repos.body()!!
//            } else {
//                throw NetworkState.InvalidData(empty = "No data on server")
//            }
//        } catch (e: Exception){
//            throw NetworkState.NetworkException(error = e)
//        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getStarGroup(
        repoName: String,
        ownerName: String,
        pageNumber: Int
    ): List<RemoteStarDate> {
        try {
            val tmp = gitRetrofitService.getStarUsers(
                repoName = repoName,
                ownerLogin = ownerName,
                page = pageNumber
            )
            if (tmp.isSuccessful && tmp.body() != null) {
                return tmp.body()!!
            } else {
                throw NetworkState.InvalidData(empty = "No data on server")
            }
        }catch (e: Exception){
            throw NetworkState.NetworkException(error = e)
        }
    }
}