package com.example.gitobserverapp.utils

sealed class LoadState{
    object Loading : LoadState()
    object Stopped : LoadState()
}