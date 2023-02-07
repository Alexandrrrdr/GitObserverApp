package com.example.gitobserverapp.data.remote.model

sealed class GitResponse<T>(val data: T? = null, val message: String? = null) {

    class Success<T>(data: T) :
        GitResponse<T>(data = data)

    class Error<T>(data: T? = null, message: String? = null) :
        GitResponse<T>(data = data, message = message)
}