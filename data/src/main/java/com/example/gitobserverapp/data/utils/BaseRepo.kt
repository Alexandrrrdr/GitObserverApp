package com.example.gitobserverapp.data.utils

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseRepo {
    suspend fun <T : Any> handleResponse(
        call: suspend () -> Response<T>
    ): com.example.gitobserverapp.data.remote.GitResponse<T> {
        return try {
            val response = call()
            if (response.isSuccessful && response.body() != null) {
                com.example.gitobserverapp.data.remote.GitResponse.Success(data = response.body()!!)
            } else {
                com.example.gitobserverapp.data.remote.GitResponse.Error(message = "Something wrong, no data from Server")
            }
        } catch (e: HttpException) {
            com.example.gitobserverapp.data.remote.GitResponse.Error(message = "Something went wrong HttpException")
        } catch (e: IOException) {
            com.example.gitobserverapp.data.remote.GitResponse.Exception()
        } catch (e: Throwable) {
            com.example.gitobserverapp.data.remote.GitResponse.Error(message = e.cause.toString())
        }
    }
}