//package com.example.gitobserverapp.utils.parse_period
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import com.example.gitobserverapp.data.network.model.starred.User
//import com.example.gitobserverapp.presentation.screens.barchart.BarChartModel
//import com.example.gitobserverapp.presentation.chart.model.UserModel
//import java.time.LocalDate
//import java.util.Comparator
//
//class PeriodList(){
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getYearList(list: List<UserModel>): List<BarChartModel>{
//        val tmpMatchedList = mutableListOf<BarChartModel>()
//        val tmpUsers = mutableListOf<User>()
//        val findMinStarredDate = list.minWith(Comparator.comparingInt { it.createdAt.year })
//        val todayDate = LocalDate.now().year
//        var startDate = findMinStarredDate.starredAt.year
//
//        while (startDate <= todayDate) {
//            val (match, _) = list.partition { it.starredAt.year == startDate }
//            for (i in match.indices) {
//                tmpUsers.add(match[i].user)
//            }
//            tmpMatchedList.add(
//                element = BarChartModel(
//                    period = startDate,
//                    userInfo = tmpUsers
//                )
//            )
//            tmpUsers.clear()
//            startDate++
//        }
//        return tmpMatchedList
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getMonthsPeriod(list: List<UserModel>): List<BarChartModel>{
//        val tmpMatchedList = mutableListOf<BarChartModel>()
//        val tmpUsers = mutableListOf<User>()
//        val findMinStarredDate = list.minWith(Comparator.comparingInt { it.createdAt.month.value })
//        val todayDate = LocalDate.now().month.value
//        var startDate = findMinStarredDate.starredAt.year
//
//        while (startDate <= todayDate) {
//            val (match, _) = list.partition { it.starredAt.year == startDate }
//            for (i in match.indices) {
//                tmpUsers.add(match[i].user)
//            }
//            tmpMatchedList.add(
//                element = BarChartModel(
//                    period = startDate,
//                    userInfo = tmpUsers
//                )
//            )
//            tmpUsers.clear()
//            startDate++
//        }
//        return tmpMatchedList
//    }
//
//
//}