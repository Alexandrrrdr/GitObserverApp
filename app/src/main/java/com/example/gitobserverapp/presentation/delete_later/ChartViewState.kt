package com.example.gitobserverapp.presentation.delete_later

sealed class ChartViewState{
    object Loading: ChartViewState()
    data class Error(val error: String) : ChartViewState()
    object NetworkError: ChartViewState()
    object ViewContentMain: ChartViewState()
}
