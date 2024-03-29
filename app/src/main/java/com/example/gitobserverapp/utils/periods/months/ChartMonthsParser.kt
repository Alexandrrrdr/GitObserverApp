package com.example.gitobserverapp.utils.periods.months

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.gitobserverapp.ui.screens.barchart.model.BarChartModel
import com.example.gitobserverapp.ui.screens.barchart.model.UiStarDate
import com.example.gitobserverapp.utils.Constants
import com.example.gitobserverapp.utils.Extensions.convertToLocalDate
import java.time.LocalDate

class ChartMonthsParser {

    val linkToRead = "https://medium.com/@ghaithalzein05/remote-upgrade-install-for-android-apk-file-6dbbd75554c9"
    @RequiresApi(Build.VERSION_CODES.O)
    fun monthsCreater(pieceOfList: List<UiStarDate>): List<BarChartModel> {

        var endDateYear = pieceOfList[pieceOfList.lastIndex].date.convertToLocalDate()!!.year
        var startDateYear = pieceOfList[Constants.ZERO_PAGE].date.convertToLocalDate()!!.year
        var todayDateYear = LocalDate.now().year


        val matchedListForBarChartModel = mutableListOf<BarChartModel>()

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
            usersForBarChartData.addAll(pieceOfList.filter { it.date.convertToLocalDate()!!.year == endDateYear })
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