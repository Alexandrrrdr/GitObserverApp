package com.example.gitobserverapp.data.repository

import android.util.Log
import com.example.gitobserverapp.data.remote.GitRetrofitService
import com.example.gitobserverapp.data.remote.model.GitResponse
import com.example.gitobserverapp.data.remote.model.RemoteResult
import com.example.gitobserverapp.domain.repository.GetRepoListByName
import com.example.gitobserverapp.utils.Constants.DEF_PER_PAGE
import com.example.gitobserverapp.utils.Constants.SORT_BY
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class ReposImplList @Inject constructor(
    private val gitRetrofitService: GitRetrofitService
) : GetRepoListByName {

    override suspend fun getData(searchWord: String, page: Int): GitResponse<RemoteResult> {
        return handleResponse {
            gitRetrofitService.getRepos(
                q = searchWord,
                sort = SORT_BY,
                page = page,
                per_page = DEF_PER_PAGE
            )
        }
    }

    override suspend fun saveData(gitResult: GitResponse<RemoteResult>) {
        TODO("Not yet implemented")
    }

    suspend fun <T: Any> handleResponse(
        call: suspend () -> Response<T>
    ): GitResponse<T> {
        return try {
                val response = call()
                if (response.isSuccessful) {
                    GitResponse.Success(data = response.body()!!)
                } else {
                    GitResponse.Error(message = response.message())
                }
            }
            catch (e: HttpException) {
                GitResponse.Error(message = "Something went wrong HttpException")
            }
            catch (e: IOException){
                GitResponse.Exception()
            }
            catch (e: Throwable){
                GitResponse.Error(message = "Throw exception")
            }

    }
}