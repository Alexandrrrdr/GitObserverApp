package com.example.gitobserverapp.utils.periods.years

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.gitobserverapp.ui.screens.barchart.model.BarChartModel
import com.example.gitobserverapp.ui.screens.barchart.model.UiStarDate
import com.example.gitobserverapp.utils.Constants
import com.example.gitobserverapp.utils.Extensions.convertToLocalDate
import java.time.LocalDate

class ChartYearsParser() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun yearCreater(list: List<UiStarDate>): List<BarChartModel> {

        var endDateYear = list[list.lastIndex].date.convertToLocalDate()!!.year
        var startDateYear = list[Constants.ZERO_PAGE].date.convertToLocalDate()!!.year
        var todayDateYear = LocalDate.now().year
        val matchedListForBarChartModel = mutableListOf<BarChartModel>()

        Log.d("info", "${list[list.lastIndex].date.convertToLocalDate()!!.monthValue}")
        var amountOfDates = 0
        for (i in startDateYear..todayDateYear) {
            amountOfDates++
        }

        //First -> check if list ended not in 2023
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
            val usersForBarChartData = mutableListOf<UiStarDate>()
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
                startDateYear--
            }
        } else {
            val differ = amountOfDates % 5
            if (differ != 0) {
                val tmpStartDate = startDateYear - (5 - differ)
                while (startDateYear > (tmpStartDate)) {
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