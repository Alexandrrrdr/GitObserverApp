package com.example.gitobserverapp.presentation.chart

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.gitobserverapp.databinding.FragmentChartBinding
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

        let { viewModel.getStarInfo(repoOwnerName = repoOwnerLogin!!, repoName = repoName!!, repoCreatedAt!!) }

        renderUi()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun renderUi(){
        viewModel.starredUsersLiveData.observe(viewLifecycleOwner){ list ->
//            viewModel.parseStarredData(list)
        }

        viewModel.barChartLiveData.observe(viewLifecycleOwner){ list ->
//            Snackbar.make(binding.root, "Size of list is ${list.size}", Snackbar.LENGTH_LONG).show()
//            initBarChart(list = list)
        }
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun initBarChart(list: List<ComparedList>, value: Int) {
//        when(value){
//            //weeks
//            0 -> {
//
//            }
//            //months
//            1 -> {
//
//            }
//            //years
//            2 -> {
//
//            }
//        }
//        barChart = binding.barChart
//        barChart.setVisibleXRangeMaximum(8f)
//        val value = getBarChartData(list)
//        barDataSet = BarDataSet(value, "By years")
//        barData = BarData(barDataSet)
//        barChart.data = barData
//        barDataSet.valueTextColor = Color.BLACK
//        barDataSet.valueTextSize = 10f
//        barDataSet.color = Color.DKGRAY
//    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun getBarChartData(list: List<ComparedList>): ArrayList<BarEntry> {
//        barEntriesList = arrayListOf()
//
//        for (i in list.indices){
//            val value = list[i].starred_at.year
//            barEntriesList.add(BarEntry(i.toFloat(), value.toFloat()))
//        }
//        return barEntriesList
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}