package com.example.gitobserverapp.data.remote.model

import retrofit2.Response
import kotlin.Exception

data class NetworkResponse<T>(
    val data: Response<T>?,
    val exception: Exception?,
    val status: Status,
) {

    companion object{
        fun <T> success (data: Response<T>): NetworkResponse<T>{
            return NetworkResponse(
                data = data,
                exception = null,
                status = Status.Success
            )
        }

        fun <T> failure (exception: Exception): NetworkResponse<T>{
            return NetworkResponse(
                data = null,
                exception = exception,
                status = Status.Failure
            )
        }
    }

    sealed class Status{
        object Success: Status()
        object Failure: Status()
    }
    val failed: Boolean get() = this.status == Status.Failure
    val isSuccessful: Boolean get() = !failed && this.data?.isSuccessful==true
    val body: T get() = this.data!!.body()!!
}


