package com.example.gitobserverapp.ui.screens.barchart

import com.example.gitobserverapp.data.remote.model.RemoteStarGroup

data class BarChartModel(
    var period: Int,
    var userInfo: List<com.example.gitobserverapp.data.remote.model.RemoteStarGroup>
)
