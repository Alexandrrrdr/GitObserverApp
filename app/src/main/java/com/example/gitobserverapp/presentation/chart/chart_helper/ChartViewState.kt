package com.example.gitobserverapp.presentation.chart.chart_helper

sealed class ChartViewState{
    object Loading: ChartViewState()
    data class Error(val error: String) : ChartViewState()
    object NetworkError: ChartViewState()
    object ViewContentMain: ChartViewState()
}
