package com.example.gitobserverapp.presentation.chart

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.gitobserverapp.R
import com.example.gitobserverapp.data.network.model.starred.User
import com.example.gitobserverapp.databinding.FragmentChartBinding
import com.example.gitobserverapp.presentation.InternetConnection
import com.example.gitobserverapp.presentation.chart.chart_helper.ChartState
import com.example.gitobserverapp.presentation.chart.model.BarChartModel
import com.example.gitobserverapp.presentation.chart.model.UserModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import java.time.LocalDate


class ChartFragment : Fragment() {

    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChartViewModel by activityViewModels()

    //safeArgs from main fragment
    private val args: ChartFragmentArgs by navArgs()
    private var repoName = ""
    private var repoOwnerName = ""
    private var repoCreatedAt = ""

    private val internet = InternetConnection()
    private var listUserModel = mutableListOf<UserModel>()

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

        repoName = args.repoName
        repoOwnerName = args.repoOwnerName
        repoCreatedAt = args.repoCreatedAt

        if (internet.checkInternetConnection(requireContext())) {
            let {
                viewModel.getDataFromGitHub(
                    repoOwnerName = repoOwnerName,
                    repoName = repoName,
                    createdAt = repoCreatedAt
                )
            }
        } else {
            viewModel.setErrorState("Check the internet connection")
        }
        radioButtonClick()
        renderUi()
    }

    private fun radioButtonClick() {
        binding.radioButtonGroup.setOnCheckedChangeListener { radioGroup, isChecked ->
            val checkButton = radioGroup.checkedRadioButtonId
            viewModel.setCheckedRadioButton(checkButton)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun renderUi() {
        viewModel.chartScreenState.observe(viewLifecycleOwner) { state ->
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

        viewModel.radioButtonCheckedLiveData.observe(viewLifecycleOwner) { radioModel ->
            when (radioModel.radioButton) {
                R.id.radioBtnYears -> binding.radioBtnYears.isChecked
                R.id.radioBtnMonths -> binding.radioBtnMonths.isChecked
                R.id.radioBtnWeeks -> binding.radioBtnWeeks.isChecked
            }
        }

        viewModel.barChartListLiveData.observe(viewLifecycleOwner) { barChartModelList ->
            initBarChart(barChartModelList)
        }
    }

    private fun initBarChart(list: List<BarChartModel>) {
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
        barEntryList.clear()
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
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS, 200)
        val barData = BarData(barDataSet)
        barData.barWidth = 0.5f
        return barData
    }

    private fun barChartConfiguration() {

        barChart.description.isEnabled = false
        barChart.axisRight.isEnabled = false

        barChart.isDragEnabled = true
        barChart.setVisibleXRangeMaximum(4f)
        barChart.animateY(1000)
        barChart.animateX(1000)

        val xAxis: XAxis = barChart.xAxis
        xAxis.textColor = Color.BLACK
        xAxis.textSize = 12f
        xAxis.setCenterAxisLabels(false)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)

        val axisLeft: YAxis = barChart.axisLeft
        axisLeft.granularity = 5f
        axisLeft.axisMinimum = 0f

        val axisRight: YAxis = barChart.axisRight
        axisRight.granularity = 1f
        axisRight.axisMinimum = 0f
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun compareYearsModel(list: List<UserModel>) {

        val tmpMatchedList = mutableListOf<BarChartModel>()
        val tmpUsers = mutableListOf<User>()

        val findMinStarredDate = list.minWith(Comparator.comparingInt { it.createdAt.year })
        val findMaxStarredDate = LocalDate.now().year
        var startDate = findMinStarredDate.starredAt.year

        while (startDate <= findMaxStarredDate) {
            val (match, rest) = list.partition { it.starredAt.year == startDate }
            for (i in match.indices) {
                tmpUsers.add(match[i].users)
            }
            tmpMatchedList.add(
                element = BarChartModel(
                    item = startDate,
                    amount = match.size,
                    userInfo = tmpUsers
                )
            )

            tmpUsers.clear()
            startDate++
        }
        viewModel.setBarChartYearsData(tmpMatchedList)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}