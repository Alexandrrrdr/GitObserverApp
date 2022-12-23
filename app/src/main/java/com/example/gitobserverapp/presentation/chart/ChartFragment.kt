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
import com.example.gitobserverapp.data.network.model.starred.User
import com.example.gitobserverapp.databinding.FragmentChartBinding
import com.example.gitobserverapp.presentation.InternetConnection
import com.example.gitobserverapp.presentation.MyXAxiasValueFormatter
import com.example.gitobserverapp.presentation.chart.chart_helper.ChartState
import com.example.gitobserverapp.presentation.chart.model.BarChartModel
import com.example.gitobserverapp.presentation.chart.model.UserModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.StackedValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.time.LocalDate


class ChartFragment : Fragment() {

    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChartViewModel by activityViewModels()
    private val internet = InternetConnection()
    private var listUserModel = mutableListOf<UserModel>()
    private var listOfDates = mutableListOf<BarChartModel>()

    //Start creating barCharts
    private lateinit var barChart: BarChart
    private var barEntryList = mutableListOf<BarEntry>()

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

        if (internet.checkInternetConnection(requireContext())) {
            let {
                viewModel.getDataFromGitHub(
                    repoOwnerName = repoOwnerLogin!!,
                    repoName = repoName!!,
                    repoCreatedAt!!
                )
            }
        } else {
            viewModel.setCharState("Check the internet connection")
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
    private fun renderUi() {
        viewModel.chartState.observe(viewLifecycleOwner) { state ->
            when (state) {
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

        viewModel.starredUsersLiveData.observe(viewLifecycleOwner) { userModel ->
            listUserModel.addAll(userModel)

            compareYearsModel(let { listUserModel })
        }

        viewModel.radioCheckedLiveData.observe(viewLifecycleOwner) { radioModel ->
            when (radioModel.radioButton) {
                R.id.radioBtnYears -> {

                }
                R.id.radioBtnMonths -> {

                }
                R.id.radioBtnWeeks -> {

                }
            }
        }

        viewModel.barChartListLiveData.observe(viewLifecycleOwner) { barChartList ->

            //TODO start thinking from here
            initBarChart(barChartList)
        }
    }

    private fun initBarChart(list: List<BarChartModel>) {

        //TODO make configuration
        viewModel.setBarChartData(barEntryList)
        barChart = binding.barChart
        val data = createBarChartData(list = list)
        barChartConfiguration()
        prepareBarChart(data)
    }

    private fun prepareBarChart(data: BarData) {
        data.setValueTextSize(12f)
        barChart.data = data
        barChart.invalidate()
    }

    private fun createBarChartData(list: List<BarChartModel>): BarData {
        for (i in list.indices) {
            barEntryList.add(
                BarEntry(
                    list[i].item.toFloat(),
                    list[i].amount.toFloat(),
                    list[i].userInfo
                )
            )
        }
        val barDataSet = BarDataSet(barEntryList, "Test")

//        barDataSet.valueFormatter = StackedValueFormatter()
        barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS, 200)
        return BarData(barDataSet)
    }

    private fun barChartConfiguration() {
        barChart.description.isEnabled = false
        barChart.axisRight.isEnabled = false

        barChart.setDrawValueAboveBar(false)
        barChart.setDrawGridBackground(false)
        barChart.setDrawBarShadow(false)


        barChart.animateY(1000)
        barChart.animateX(1000)

        val xAxis: XAxis = barChart.xAxis
        xAxis.textColor = Color.BLACK
        xAxis.textSize = 10f

        xAxis.spaceMin = 0.5f
        xAxis.spaceMax = 0.5f
        xAxis.labelCount = 0
        xAxis.isEnabled = true
        xAxis.position = XAxis.XAxisPosition.TOP_INSIDE
        xAxis.setDrawGridLines(false)

        val axisLeft: YAxis = barChart.axisLeft
        axisLeft.granularity = 10f
        axisLeft.axisMinimum = 0f

        val axisRight: YAxis = barChart.axisRight
        axisRight.granularity = 10f
        axisRight.axisMinimum = 0f
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun compareYearsModel(list: List<UserModel>) {

        val tmpList = mutableListOf<BarChartModel>()
        val tmpUsers = mutableListOf<User>()

        val findMinStarredDate = list.minWith(Comparator.comparingInt { it.createdAt.year })
        val findMaxStarredDate = LocalDate.now().year
        var startDate = findMinStarredDate.starredAt.year

        while (startDate <= findMaxStarredDate) {
            val filtered = list.filter { e -> e.createdAt.year == startDate }
            for (i in filtered.indices){
                tmpUsers.add(filtered[i].users)
            }
            tmpList.add(element = BarChartModel(item = startDate, amount = filtered.size, userInfo = tmpUsers))
            tmpUsers.clear()
            startDate++
        }
        println(tmpList.size)
        viewModel.setBarChartYearsData(tmpList)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}