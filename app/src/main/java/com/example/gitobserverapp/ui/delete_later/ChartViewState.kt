package com.example.gitobserverapp.ui.delete_later

sealed class ChartViewState{
    object Loading: ChartViewState()
    data class Error(val error: String) : ChartViewState()
    object NetworkError: ChartViewState()
    object ViewContentMain: ChartViewState()
}
