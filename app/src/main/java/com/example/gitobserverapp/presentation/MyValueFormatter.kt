package com.example.gitobserverapp.presentation

import com.example.gitobserverapp.presentation.chart.model.BarChartModel
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class MyXAxiasValueFormatter(private val list: List<BarChartModel>): ValueFormatter() {

    private val days = arrayOf("Mo", "Tu", "Wed", "Th", "Fr", "Sa", "Su")

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return list.getOrNull(value.toInt()).toString()
    }
}