package com.example.gitobserverapp.presentation.chart.chart_helper

sealed class ChartState{
    object Loading: ChartState()
    data class Error(val error: String) : ChartState()
    object NetworkError: ChartState()
    object ViewContentMain: ChartState()
}
