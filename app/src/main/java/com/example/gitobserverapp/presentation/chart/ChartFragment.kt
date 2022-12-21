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
import com.example.gitobserverapp.data.network.model.starred.User
import com.example.gitobserverapp.databinding.FragmentChartBinding
import com.example.gitobserverapp.presentation.chart.model.UserModel
import com.example.gitobserverapp.presentation.chart.model.BarChartModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import java.time.LocalDate

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
    private var list = listOf<UserModel>()

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
            comparedModelCreate(chartModel)
        }

        viewModel.barDataSet.observe(viewLifecycleOwner){ barDataSet ->

        }

        viewModel.radioCheckedLiveData.observe(viewLifecycleOwner){ button ->
            radioBtnId = button.radioButton
        }

        viewModel.barChartYearsLiveData.observe(viewLifecycleOwner){ comparedModel ->
            initBarChart(comparedModel)
        }
    }

    private fun initBarChart(list: List<BarChartModel>) {

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
    fun comparedModelCreate(list: List<UserModel>) {

        val startDate = list[0].createdAt
        val currentDate = LocalDate.now()
        var counter = 0
        var index = 0
        val compareList = arrayListOf<BarChartModel>()
        val userList = listOf<User>()

        for (i in startDate.year..currentDate.year) {
            counter = 0
            for (y in list.indices) {
                if (i == list[y].starredAt.year) {
                    counter++

                    //TODO Change the logic
                    userList[index].copy(id = list[y].users.id, login = list[y].users.login)

                    compareList.add(
                        BarChartModel(
                            item = i,
                            amount = counter,
                            userInfo = userList
                        )
                    )
                }
            }
        }
        Log.d("chart", "compared list size is ${compareList.size}")
        viewModel.setBarChartYearsData(compareList)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}