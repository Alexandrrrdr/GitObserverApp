package com.example.gitobserverapp.presentation.chart

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.gitobserverapp.R
import com.example.gitobserverapp.data.network.model.starred.User
import com.example.gitobserverapp.databinding.FragmentChartBinding
import com.example.gitobserverapp.presentation.InternetConnection
import com.example.gitobserverapp.presentation.chart.chart_helper.ChartState
import com.example.gitobserverapp.presentation.chart.model.UserModel
import com.example.gitobserverapp.presentation.chart.model.BarChartModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate

class ChartFragment : Fragment() {

    private var _binding: FragmentChartBinding?=null
    private val binding get() = _binding!!

    private val viewModel: ChartViewModel by activityViewModels()
    private val internet = InternetConnection()
    private var listUserModel = mutableListOf<UserModel>()
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

        if (internet.checkInternetConnection(requireContext())){
            let { viewModel.getDataFromGitHub(repoOwnerName = repoOwnerLogin!!, repoName = repoName!!, repoCreatedAt!!) }
        } else {
            viewModel.setCharState("Check internet connection")
        }
        radioButtonClick()
        renderUi()
    }

    private fun radioButtonClick() {
        binding.radioButtonGroup.setOnCheckedChangeListener { radioGroup, isChecked ->
            val checkButton = radioGroup.checkedRadioButtonId
            viewModel.setCheckedRadioButton(checkButton)
            Log.d("button", "Button check is $checkButton")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun renderUi(){
        viewModel.chartState.observe(viewLifecycleOwner){ state ->
            when(state){
                is ChartState.Error -> {
                    binding.txtInfo.visibility = View.VISIBLE
                    binding.txtInfo.text = state.error
                    binding.progBarChart.visibility = View.GONE
                }
                ChartState.Loading -> {
                    binding.txtInfo.visibility = View.GONE
                    binding.progBarChart.visibility = View.VISIBLE
                }
                is ChartState.ViewContentMain -> {
                    binding.txtInfo.visibility = View.GONE
                    binding.progBarChart.visibility = View.GONE
                }
            }
        }

        viewModel.starredUsersLiveData.observe(viewLifecycleOwner){ userModel ->
            listUserModel.addAll(userModel)
        }

        viewModel.radioCheckedLiveData.observe(viewLifecycleOwner){ radioModel ->
            radioModel.let {
                when(it.radioButton){
                    R.id.radioBtnYears -> {
                        compareYearsModel(let { listUserModel })
                    }
                    R.id.radioBtnMonths -> {

                    }
                    R.id.radioBtnWeeks -> {

                    }
                }
            }
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

        val findMinStarredDate = list.minWith(Comparator.comparingInt { it.createdAt.year })
        val findMaxStarredDate = LocalDate.now().year
        var startDate = findMinStarredDate.starredAt.year
        var tmpCounter = 0

        //TODO not correctly counting amount
                while (startDate <= findMaxStarredDate) {
                    val filtered = list.filter { e -> e.createdAt.year == startDate }
                    if (filtered.isNotEmpty()){
                        tmpCounter = filtered.size
                        for (i in filtered.indices){
                            tmpUsers.add(filtered[i].users)
                            tmpCounter = filtered.size
                        }
                    }
                    tmpList.add(BarChartModel(item = startDate, amount = tmpCounter, userInfo = tmpUsers))
                    startDate++
                    tmpCounter = 0
                }
        Log.d("chart", tmpList.size.toString())
        viewModel.setBarChartYearsData(tmpList)
        tmpList.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}