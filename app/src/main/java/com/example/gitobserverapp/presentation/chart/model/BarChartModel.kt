package com.example.gitobserverapp.presentation.chart.model

import com.example.gitobserverapp.data.network.model.stargazers.User

data class BarChartModel(
    var period: Int,
    var userInfo: List<User>
)
