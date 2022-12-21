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
import com.example.gitobserverapp.R
import com.example.gitobserverapp.databinding.FragmentChartBinding
import com.example.gitobserverapp.presentation.chart.model.ChartModel
import com.example.gitobserverapp.presentation.chart.model.ComparedModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlin.collections.ArrayList

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
        val repoCreatedAt: String? = arguments?.getString("created_at", "1970-12-20")
        binding.repoName.text = "$repoName"

        let { viewModel.getDataFromGitHub(repoOwnerName = repoOwnerLogin!!, repoName = repoName!!, repoCreatedAt!!) }
        renderUi()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun renderUi(){
        viewModel.starredUsersLiveData.observe(viewLifecycleOwner){ chartModel ->
            binding.repoName.text = chartModel[0].repoName

        }
        viewModel.radioCheckedLiveData.observe(viewLifecycleOwner){ radioModel ->

        }

        viewModel.barDataSet.observe(viewLifecycleOwner){ barDataSet ->
            binding.barChart.data = BarData(barDataSet)
        }

        viewModel.barChartYearsLiveData.observe(viewLifecycleOwner){ comparedModel ->
            checkButtons(comparedModel)
        }
    }

//    private fun initBarChart(list: List<ComparedModel>) {
//        barChart = binding.barChart
//        barChart.setVisibleXRangeMaximum(8f)
//        val value = getBarChartData(list)
//        barDataSet = BarDataSet(value, "By years")
//        barData = BarData(barDataSet)
//        barDataSet.valueTextColor = Color.BLACK
//        barDataSet.valueTextSize = 10f
//        barDataSet.color = Color.DKGRAY
//        barChart.data = barData
//    }
//
//    private fun getBarChartData(list: List<ComparedModel>): ArrayList<BarEntry> {
//        barEntriesList = arrayListOf()
//
//        for (i in list.indices){
//            val value = list[i].amount
//            barEntriesList.add(BarEntry(i.toFloat(), value.toFloat()))
//        }
//        return barEntriesList
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkButtons(list: List<ComparedModel>){
        viewModel.initBarChart(list)

        binding.radioButtonGroup.setOnCheckedChangeListener { radioGroup, checkId ->
            when(checkId){
                R.id.radioBtnYears -> {
                    viewModel.setCheckedRadioButton(R.id.radioBtnYears)
//                    viewModel.dateComparator(list = list, keyValue = 0)
                }
                R.id.radioBtnMonths -> {
                    //TODO radio months button
                }
                R.id.radioBtnWeeks -> {
                    //TODO radio weeks button
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}