package com.example.gitobserverapp.presentation.chart

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
import com.example.gitobserverapp.data.network.model.starred.User
import com.example.gitobserverapp.databinding.FragmentChartBinding
import com.example.gitobserverapp.presentation.chart.model.UserModel
import com.example.gitobserverapp.presentation.chart.model.BarChartModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class ChartFragment : Fragment() {

    private var _binding: FragmentChartBinding?=null
    private val binding get() = _binding!!

    private val viewModel: ChartViewModel by activityViewModels()

    //Start creating barCharts
    private lateinit var barChart: BarChart
    private lateinit var barDataSet: BarDataSet
    private lateinit var barEntryList: ArrayList<BarEntry>
    private lateinit var barData: BarData
    private var radioBtnId: Int = 0
    private var tmpList = mutableListOf<BarChartModel>()

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

        let { viewModel.getDataFromGitHub(repoOwnerName = repoOwnerLogin!!, repoName = repoName!!, repoCreatedAt!!) }
        renderUi()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun renderUi(){
        viewModel.starredUsersLiveData.observe(viewLifecycleOwner){ chartModel ->
            binding.repoName.text = chartModel[0].repoName
            compareYearsModel(chartModel)
        }

        viewModel.barDataSet.observe(viewLifecycleOwner){ barDataSet ->

        }

        viewModel.radioCheckedLiveData.observe(viewLifecycleOwner){ button ->
            when(button.radioButton) {
                R.id.radioBtnYears -> {
                    initBarChart(tmpList)
                }
            }
        }

        viewModel.barChartYearsLiveData.observe(viewLifecycleOwner){ barChartModel ->
            tmpList.addAll(barChartModel)
        }
    }

    private fun initBarChart(list: List<BarChartModel>) {

        //TODO doesn't work! Check it again!
        for (i in list.indices) {
            barEntryList.add(BarEntry(list[i].item.toFloat(), list[i].amount.toFloat()))
        }
        barDataSet = BarDataSet(barEntryList, "Years")
        barData = BarData(barDataSet)
        barChart = binding.barChart
        barChart.setVisibleXRangeMaximum(8f)
        barChart.description.isEnabled = false
        barChart.isHorizontalScrollBarEnabled = true
        binding.barChart.data = barData
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun compareYearsModel(list: List<UserModel>) {
        val tmpList = mutableListOf<BarChartModel>()
        val tmpUsers = mutableListOf<User>()

        val findMinStarredDate = list.minWith(Comparator.comparingInt { it.starredAt.year })
        val findMaxStarredDate = list.maxWith(Comparator.comparingInt { it.starredAt.year })
        var startDate = findMinStarredDate.starredAt.year

        //TODO not correctly counting amount
                while (startDate <= findMaxStarredDate.starredAt.year) {
                    val filtered = list.filter { it.createdAt.year == startDate }
                    for (i in filtered.indices){
                        tmpUsers.add(filtered[i].users)
                    }
                    tmpList.add(BarChartModel(item = startDate, amount = filtered.size, userInfo = tmpUsers))
                    tmpUsers.clear()
                    startDate++
                }

        viewModel.setBarChartYearsData(tmpList)
        Log.d("chart", "size of barchart list is ${tmpList.size}")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}