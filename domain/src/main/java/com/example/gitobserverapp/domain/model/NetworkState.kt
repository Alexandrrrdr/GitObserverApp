package com.example.gitobserverapp.domain.model

sealed class NetworkState: Exception() {
    data class NetworkException(val error : Throwable) : NetworkState()
    data class InvalidData(val empty: String): NetworkState()
}
