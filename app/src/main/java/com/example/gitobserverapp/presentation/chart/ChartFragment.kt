package com.example.gitobserverapp.presentation.chart

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repoOwnerLogin: String? = arguments?.getString("owner_name", "No owner name")
        val repoName: String? = arguments?.getString("repo_name", "No repo name")
        binding.repoName.text = "$repoName"

        let { viewModel.getStarInfo(repoOwnerName = repoOwnerLogin!!, repoName = repoName!!) }

        renderUi()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun renderUi(){
        viewModel.chartLiveData.observe(viewLifecycleOwner){ list ->
            viewModel.parseStarredData(list)
        }

        viewModel.starredLiveData.observe(viewLifecycleOwner){ list ->
            initBarChart(list = list)
        }
    }

    private fun initBarChart(list: List<StarParsedModel>) {
        barChart = binding.barChart
        getBarChartData(list)
        barDataSet = BarDataSet(barEntriesList, "Test")
        barData = BarData(barDataSet)
        barChart.data = barData
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 10f
        barDataSet.color = Color.MAGENTA
    }

    private fun getBarChartData(list: List<StarParsedModel>) {
        barEntriesList = arrayListOf()

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