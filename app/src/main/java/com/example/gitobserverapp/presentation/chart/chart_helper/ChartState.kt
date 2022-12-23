package com.example.gitobserverapp.presentation.chart.chart_helper

import com.example.gitobserverapp.data.network.model.starred.StarredModelItem

sealed class ChartState{
    object Loading: ChartState()
    data class Error(val error: String) : ChartState()
    data class ViewContentMain(val result: List<StarredModelItem>): ChartState()
}
