package com.example.gitobserverapp.utils.parse_period.years

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.gitobserverapp.ui.screens.barchart.model.BarChartModel
import com.example.gitobserverapp.ui.screens.barchart.model.UiStarGroup
import com.example.gitobserverapp.utils.Constants
import com.example.gitobserverapp.utils.Extensions.convertToLocalDate
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class YearParser() {

    @RequiresApi(Build.VERSION_CODES.O)
    private fun compareYearsModel(list: List<UiStarGroup>): List<BarChartModel> {

        var endDateYear = list[list.lastIndex].date.convertToLocalDate()!!.year

        var startDateYear = list[Constants.ZERO_PAGE].date.convertToLocalDate()!!.year
        var todayDateYear = LocalDate.now().year
        val matchedListForBarChartModel = mutableListOf<BarChartModel>()
        var amountOfDates = 0
        for (i in startDateYear..todayDateYear) {
            amountOfDates++
        }

        //If last stargazers starred date less than today year 2023 i fill empty data until starred
        if (todayDateYear > endDateYear) {
            while (todayDateYear > endDateYear) {
                matchedListForBarChartModel.add(
                    element = BarChartModel(
                        period = todayDateYear,
                        userInfo = emptyList()
                    )
                )
                todayDateYear--
            }
        }

        //stargazers started
        while (endDateYear >= startDateYear) {
            val usersForBarChartData = mutableListOf<UiStarGroup>()
            usersForBarChartData.addAll(list.filter { it.date.convertToLocalDate()!!.year == endDateYear })
            matchedListForBarChartModel.add(
                element = BarChartModel(
                    period = endDateYear,
                    userInfo = usersForBarChartData
                )
            )
            endDateYear--
        }

        if (amountOfDates < 5) {
            val tmpLeftDates = 5 - amountOfDates
            for (i in 1..tmpLeftDates) {
                matchedListForBarChartModel.add(
                    element = BarChartModel(
                        period = startDateYear,
                        userInfo = emptyList()
                    )
                )
            }
        } else {
            val differ = amountOfDates % 5
            if (differ != 0) {
                val tmpStartDay = startDateYear - (5 - differ)
                while (startDateYear > (tmpStartDay)) {
                    startDateYear--

                    matchedListForBarChartModel.add(
                        element = BarChartModel(
                            period = startDateYear,
                            userInfo = emptyList()
                        )
                    )
                }
            }
        }
        return matchedListForBarChartModel
    }
}