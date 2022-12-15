package com.example.gitobserverapp.presentation.chart

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.gitobserverapp.databinding.FragmentChartBinding
import com.example.gitobserverapp.presentation.chart.model.ChartModel
import com.example.gitobserverapp.presentation.chart.model.StarParsedModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlin.random.Random

class ChartFragment : Fragment() {

    private var _binding: FragmentChartBinding?=null
    private val binding get() = _binding!!

    private val viewModel: ChartViewModel by activityViewModels()

    //Start creating barCharts
    private lateinit var barChart: BarChart
    private lateinit var barData: BarData
    private lateinit var barDataSet: BarDataSet
    private lateinit var barEntriesList: ArrayList<BarEntry>

   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repoOwnerLogin: String? = arguments?.getString("owner_name", "No owner name")
        val repoName: String? = arguments?.getString("repo_name", "No repo name")
        binding.repoName.text = "$repoName"

        let { viewModel.getStarInfo(repoOwnerName = repoOwnerLogin!!, repoName = repoName!!) }
        renderUi()

    }

    private fun renderUi(){
        viewModel.chartLiveData.observe(viewLifecycleOwner){ list ->
            //TODO fill data to Chart
//            initBarChart(list as ArrayList<ChartModel> /* = java.util.ArrayList<com.example.gitobserverapp.presentation.chart.model.ChartModel> */)
//            Log.d("chart", "size is ${list.size.toString()}")
            viewModel.parseStarredData(list)
        }
        viewModel.starParsedLiveData.observe(viewLifecycleOwner){
            Log.d("chart", it[0].date.toString())
        }
    }

    private fun initBarChart(list: ArrayList<ChartModel>) {
        barChart = binding.barChart
        getBarChartData(list)
        barDataSet = BarDataSet(barEntriesList, "Test")
        barData = BarData(barDataSet)
        barChart.data = barData
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 10f
        barDataSet.color = Color.MAGENTA
    }

    private fun getBarChartData(list: ArrayList<ChartModel>) {
//        barEntriesList = ArrayList()

        // on below line we are adding data
        // to our bar entries list
//        for (i in list.indices){
//            var value = list[i].starredAt
//            barEntriesList.add(BarEntry(i.toFloat(), generateRandomDouble()))
//        }
    }

    private fun generateRandomDouble(): Float {
        return Random.nextInt(10).toFloat()
    }

    private fun generateRandomChartColor(): Float{
        return Random.nextInt(256).toFloat()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}