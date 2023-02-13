package com.example.gitobserverapp.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.gitobserverapp.data.remote.GitRetrofitService
import com.example.gitobserverapp.domain.model.StarGroup
import com.example.gitobserverapp.domain.repository.GetStars
import javax.inject.Inject

class StarsImpl
@Inject constructor(private val gitRetrofitService: GitRetrofitService)
    : GetStars{

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getData(
        repo_name: String,
        owner_login: String,
        page_number: Int
    ): List<StarGroup> {
        return emptyList()

//
//        var requestResult = loadPageAndNext(
//            gitRetrofitService = gitRetrofitService,
//            repo_name = repo_name,
//            owner_login = owner_login,
//            page_number = page_number
//        )
//
//        var tmpPage = 2
//        while (requestResult.data?.isNotEmpty() == true) {
//            tmpList.addAll(requestResult.data!!)
//            requestResult = loadPageAndNext(
//                gitRetrofitService = gitRetrofitService,
//                repo_name = repo_name,
//                owner_login = owner_login,
//                page_number = tmpPage
//            )
//            tmpPage++
//        }

//        val tmpList = mutableListOf<RemoteStarGroup>()
//        val tmp = loadPageAndNext(
//            gitRetrofitService = gitRetrofitService,
//            repo_name = repo_name,
//            owner_login = owner_login,
//            page_number = page_number
//        )
//        when(tmp){
//            is com.example.gitobserverapp.data.remote.GitResponse.Result.Exception -> {
//                return Result.Exception()
//            }
//            is com.example.gitobserverapp.data.remote.GitResponse.Result.Error -> {
//                return Result.Error(message = tmp.error.toString())
//            }
//            is com.example.gitobserverapp.data.remote.GitResponse.Result.Success -> {
//                var tmpPage = 2
//                while (tmp.data?.isNotEmpty() == true){
//                    loadPageAndNext(
//                        gitRetrofitService = gitRetrofitService,
//                        repo_name = repo_name,
//                        owner_login = owner_login,
//                        page_number = tmpPage)
//                    tmpPage++
//                    tmpList.addAll(tmp.data)
//                }
//            }
//        }
//        return Result.Success(data = tmpList)
//    }
//
//    override suspend fun saveData(starGroupList: List<RemoteStarGroup>) {
//        TODO("Not yet implemented")
//    }
//
//    private suspend fun loadPageAndNext(
//        gitRetrofitService: GitRetrofitService,
//        repo_name: String,
//        owner_login: String,
//        page_number: Int
//    ): Result<List<RemoteStarGroup>> {
//        return handleResponse { gitRetrofitService.getStarredData(
//            repo_name = repo_name,
//            owner_login = owner_login,
//            per_page = MAX_PER_PAGE,
//            page = page_number
//        )}
//    }

    }
}