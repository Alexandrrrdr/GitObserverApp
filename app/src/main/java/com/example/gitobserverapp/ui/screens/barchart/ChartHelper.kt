package com.example.gitobserverapp.ui.screens.barchart

import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.gitobserverapp.ui.screens.barchart.model.BarChartModel
import com.example.gitobserverapp.ui.screens.barchart.model.UiStarGroup
import com.example.gitobserverapp.ui.screens.details.User
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate

class ChartHelper(
    private var barChart: BarChart,
    private val listener: Listener
) {
    private lateinit var barDataSet: BarDataSet
    private lateinit var barchartGraph: BarChart
    private var barEntryList = mutableListOf<BarEntry>()
    private var barLabelList = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun initBarChart(list: List<BarChartModel>): BarChart{

        barchartGraph = barChart
        createBarChartData(list)
        barDataSet = BarDataSet(barEntryList, "")
        val barData = BarData(barDataSet)

        //Hide unnecessary labels if no data in AXis
        barData.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (value > 0) {
                    super.getFormattedValue(value)
                } else {
                    ""
                }
            }
        })

        barchartGraph.data = barData
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS, 250)
        barDataSet.valueTextSize = 14f
        barDataSet.valueTextColor = Color.BLACK
        barchartGraph.description.isEnabled = false
        barchartGraph.axisRight.isEnabled = false
        barchartGraph.isDragEnabled = false

        barchartGraph.setVisibleXRangeMaximum(5f)
        barchartGraph.setVisibleXRangeMinimum(5f)
        barchartGraph.animateY(1000)
        barchartGraph.animateX(1000)

        val xAxis: XAxis = barchartGraph.xAxis
        //set labels
        xAxis.valueFormatter = IndexAxisValueFormatter(barLabelList)
        xAxis.setCenterAxisLabels(false)
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = Color.BLACK
        xAxis.textSize = 14f

        val axisLeft: YAxis = barchartGraph.axisLeft
        axisLeft.granularity = 1f
        axisLeft.axisMinimum = 0f

        barchartGraph.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                //getting index of bar is selected
                val x = e!!.x.toInt()
                val year = barLabelList[x]
                val userList: Any? = barEntryList[x].data

                //Check List is List and same type and send safeArgs and Open new Fragment
                if (userList is List<*>) {
                    getViaPoints(userList)?.let { list ->
                        if (list.isNotEmpty()) {
                            val tmp: List<User> = list.map { User(
                                id = it.users.id,
                                login = it.users.name,
                                avatar_url = it.users.userAvaUrl)
                            }
                            listener.click(item = tmp, year = year)
                        }
                    }
                }
            }
            override fun onNothingSelected() {
                listener.nothingClick()
            }
        })
        return barchartGraph
    }

    //Check BarChart data.object after click on line
    private fun getViaPoints(list: List<*>): List<UiStarGroup>? {
        list.forEach { if (it !is UiStarGroup) return null }
        return list.filterIsInstance<UiStarGroup>()
            .apply { if (size != list.size) return null }
    }

    private fun createBarChartData(rightList: List<BarChartModel>) {
        barEntryList.clear()
        barLabelList.clear()
        val leftList = rightList.reversed()
        for (i in leftList.indices) {
            barLabelList.add(i, leftList[i].period.toString())
            if (leftList[i].userInfo.isEmpty()){
                barEntryList.add(
                    BarEntry(
                        i.toFloat(),
                        leftList[i].userInfo.size.toFloat()
                    )
                )
            } else {
                barEntryList.add(
                    BarEntry(
                        i.toFloat(),
                        leftList[i].userInfo.size.toFloat(),
                        leftList[i].userInfo
                    )
                )
            }
        }
    }

    interface Listener{
        fun click(item: List<User>, year: String)
        fun nothingClick()
    }
}