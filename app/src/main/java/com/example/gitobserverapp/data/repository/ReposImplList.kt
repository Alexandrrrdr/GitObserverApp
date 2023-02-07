package com.example.gitobserverapp.data.repository

import com.example.gitobserverapp.data.remote.GitRetrofitService
import com.example.gitobserverapp.data.remote.model.NetworkResponse
import com.example.gitobserverapp.data.remote.model.RemoteRepo
import com.example.gitobserverapp.domain.model.DomainReposListModel
import com.example.gitobserverapp.domain.model.Items
import com.example.gitobserverapp.domain.repository.GetRepoListByName
import com.example.gitobserverapp.ui.screens.main.MainRepo
import com.example.gitobserverapp.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class ReposImplList @Inject constructor(
    private val gitRetrofitService: GitRetrofitService
) : GetRepoListByName {

    override suspend fun getData(searchWord: String, page: Int): NetworkResponse<List<MainRepo>> {
        val tmpList = emptyList<Items>()
        var apiResult: Response<List<RemoteRepo>>
        CoroutineScope(Dispatchers.IO).launch {
            try {
                apiResult = async {
                    gitRetrofitService.getRepos(
                        q = searchWord,
                        sort = Constants.SORT_BY,
                        page = page,
                        per_page = Constants.DEF_PER_PAGE
                    )
                }.await()
                NetworkResponse.success(data = apiResult)
            } catch (e: Exception) {

            }

        }
        return
    }

//        CoroutineScope(Dispatchers.IO).launch {
//            val apiResult = gitRetrofitService.getRepos(
//                q = searchWord,
//                sort = Constants.SORT_BY,
//                page = page,
//                per_page = Constants.DEF_PER_PAGE
//            )
//            when (apiResult.code()) {
//                200 -> DataToDomainRepoListMapper().map(apiResult.body()!!)
//            }
//        }
//        return DomainReposListModel(hasNetwork = true, tmpList)

//    private inline fun <T> safeApiCall(apiCall: () -> Response<T>): NetworkResponse<T> {
//        return try {
//            NetworkResponse.success(data = apiCall.invoke())
//        } catch (e: Exception) {
//            NetworkResponse.failure(exception = e)
//        }
//    }


    override suspend fun saveData(domainReposListModel: DomainReposListModel) {
        TODO("Not yet implemented")
    }

}