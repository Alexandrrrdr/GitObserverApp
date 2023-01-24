package com.example.gitobserverapp.utils

import com.github.mikephil.charting.charts.BarChart

object Constants {
    const val API_BASE_URL = "https://api.github.com"
    const val API_GET_REPOS = "/search/repositories"
    const val SORT_BY = "stars"
    const val DEF_ZERO_PAGE = 0
    const val ORDER = "asc"
    const val MAX_PER_PAGE = 100
    const val DEF_PER_PAGE = 30
    const val START_PAGE = 1
    const val ZERO_PAGE = 0
    const val GIT_TOKEN = "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"

    const val MAX_X_VALUE = 13
    const val GROUPS = 2
    const val GROUP_1_LABEL = "Orders"
    const val GROUP_2_LABEL = ""
    const val BAR_SPACE = 0.1f
    const val BAR_WIDTH = 0.8f

    const val LOAD_DATA = "LOAD_DATA"
    const val SET_CONTENT = "SET_CONTENT"

}