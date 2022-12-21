package com.example.gitobserverapp.presentation.chart.model

import com.example.gitobserverapp.data.network.model.starred.User

data class BarChartModel(
    val item: Int,
    val amount: Int,
    val userInfo: List<User>
)
