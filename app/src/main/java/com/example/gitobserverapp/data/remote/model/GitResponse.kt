package com.example.gitobserverapp.data.remote.model

sealed class GitResponse<T: Any>(
    val data: T? = null,
    val error: String? = null
) {
    class Success<T: Any>(data: T): GitResponse<T>(data = data)
    class Error<T: Any>(message: String): GitResponse<T>(error =  message)
    class Exception<T: Any>(): GitResponse<T>()
}