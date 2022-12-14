package com.example.gitobserverapp.presentation.chart.model

import com.example.gitobserverapp.data.network.model.starred.User

data class BarChartModel(
    var period: Int,
    var amount: Int,
    var userInfo: List<User>
)
