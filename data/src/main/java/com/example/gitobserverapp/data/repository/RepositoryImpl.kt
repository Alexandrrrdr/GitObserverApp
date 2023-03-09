package com.example.gitobserverapp.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.gitobserverapp.data.remote.GitRetrofitService
import com.example.gitobserverapp.data.remote.model.RemoteRepo
import com.example.gitobserverapp.data.remote.model.RemoteSortedStars
import com.example.gitobserverapp.data.remote.model.RemoteStarDate
import com.example.gitobserverapp.data.utils.Constants.PERIOD
import com.example.gitobserverapp.data.utils.Constants.START_PAGE
import com.example.gitobserverapp.data.utils.Constants.ZERO_INDEX
import com.example.gitobserverapp.data.utils.Extensions.convertToLocalDate
import com.example.gitobserverapp.domain.model.NetworkState
import com.example.gitobserverapp.domain.repository.GetRepository
import retrofit2.Response
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val gitRetrofitService: GitRetrofitService
) : GetRepository {

    override suspend fun getRepos(
        userName: String
    ): List<RemoteRepo> {
        try {
            val repos = gitRetrofitService.getOwnerRepos(
                userName = userName,
                page = START_PAGE,
            )
            if (repos.isSuccessful && repos.body()!= null) {
                return repos.body()!!
            } else {
                throw NetworkState.InvalidData(empty = "No data on server")
            }
        } catch (e: Exception){
            throw NetworkState.NetworkException(error = e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getStarGroup(
        repoName: String,
        ownerName: String,
        lastPage: Int
    ): RemoteSortedStars {

        val tmpStarList = mutableListOf<RemoteStarDate>()
        var lastLoadPage = lastPage
        var isLoadAllowed = lastLoadPage > START_PAGE

        try {
            var starredList = loadData(
                repoName = repoName,
                ownerName = ownerName,
                lastPage = lastPage
            )
            if (starredList.isSuccessful && starredList.body() != null) {
                tmpStarList.addAll(ZERO_INDEX, starredList.body()!!)
                lastLoadPage --

                while (!dateRangeChecker(starredList.body()!!) && lastLoadPage >= START_PAGE){
                    starredList = loadData(repoName = repoName, ownerName = ownerName, lastPage = lastLoadPage)
                    tmpStarList.addAll(ZERO_INDEX, starredList.body()!!)
                    lastLoadPage --
                    isLoadAllowed = lastLoadPage > START_PAGE
                }
                return RemoteSortedStars(lastPage = lastLoadPage, isLoadAvailable = isLoadAllowed, list = tmpStarList)
            } else {
                throw NetworkState.InvalidData(empty = "No data on server")
            }
        }catch (e: Exception){
            throw NetworkState.NetworkException(error = e)
        }
    }

    private suspend fun loadData(
        repoName: String,
        ownerName: String,
        lastPage: Int
    ): Response<List<RemoteStarDate>> {
        return gitRetrofitService.getStarUsers(
            repoName = repoName,
            ownerLogin = ownerName,
            page = lastPage
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateRangeChecker(list: List<RemoteStarDate>): Boolean{
        val endDateYear = list[list.lastIndex].date.convertToLocalDate()!!.year
        val startDateYear = list[ZERO_INDEX].date.convertToLocalDate()!!.year
        val difference = (endDateYear - startDateYear) / PERIOD
        return difference >= START_PAGE
    }
}