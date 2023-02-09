package com.example.gitobserverapp.utils

import com.example.gitobserverapp.data.remote.GitResponse
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseRepo {
    suspend fun <T : Any> handleResponse(
        call: suspend () -> Response<T>
    ): GitResponse<T> {
        return try {
            val response = call()
            if (response.isSuccessful && response.body() != null) {
                GitResponse.Success(data = response.body()!!)
            } else {
                GitResponse.Error(message = "SOmething wrong, no data from Server")
            }
        } catch (e: HttpException) {
            GitResponse.Error(message = "Something went wrong HttpException")
        } catch (e: IOException) {
            GitResponse.Exception()
        } catch (e: Throwable) {
            GitResponse.Error(message = "Throw exception")
        }
    }
}