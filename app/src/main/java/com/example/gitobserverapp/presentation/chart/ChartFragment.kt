package com.example.gitobserverapp.presentation.chart

import android.content.Context
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gitobserverapp.App
import com.example.gitobserverapp.R
import com.example.gitobserverapp.databinding.FragmentChartBinding
import com.example.gitobserverapp.presentation.chart.chart_helper.ChartViewState
import com.example.gitobserverapp.presentation.chart.model.BarChartModel
import com.example.gitobserverapp.presentation.chart.model.PresentationStargazersListItem
import com.example.gitobserverapp.presentation.details.DetailsViewModel
import com.example.gitobserverapp.utils.network.NetworkStatusHelper
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class ChartFragment : Fragment() {

    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    //safeArgs from main fragment
    private val args: ChartFragmentArgs by navArgs()
    private var repoName = ""
    private var repoOwnerName = ""
    private var repoCreatedAt = ""
    private var page = 1

    //    private lateinit var internet: InternetConnectionLiveData
    private lateinit var networkStatus: NetworkStatusHelper

    //Start creating barCharts
    private lateinit var barDataSet: BarDataSet
    private lateinit var barchartGraph: BarChart
    private var barEntryList = mutableListOf<BarEntry>()
    private var barLabelList = mutableListOf<String>()


    private lateinit var chartViewModel: ChartViewModel
    @Inject
    lateinit var chartViewModelFactory: ChartViewModelFactory
    private val detailsViewModel: DetailsViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).appComponent.inject(this@ChartFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        networkStatus = NetworkStatusHelper(requireContext())
        chartViewModel = ViewModelProvider(this, chartViewModelFactory)[ChartViewModel::class.java]
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repoName = args.repoName
        repoOwnerName = args.repoOwnerName
        repoCreatedAt = args.repoCreatedAt
        binding.repoName.text = repoName

        chartViewModel.setSearchLiveData(repoOwnerName = repoOwnerName, repoName = repoName)

        radioButtonClick()
        nextPageClick()
        prevPageClick()
        renderUi()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun prevPageClick() {
        binding.prevPage.setOnClickListener {
            chartViewModel.setPageObserverLiveData(page - 1)
            page--
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun nextPageClick() {
        binding.nextPage.setOnClickListener {
            chartViewModel.setPageObserverLiveData(page + 1)
            page++
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun radioButtonClick() {
        binding.radioButtonGroup.setOnCheckedChangeListener { _, isChecked ->
//            chartViewModel.setCheckedRadioButton(isChecked)
//                chartViewModel.getStargazersList()
        }

        binding.radioBtnYears.setOnClickListener {
            chartViewModel.getStargazersList()

        }

        binding.radioBtnMonths.setOnClickListener{
            Snackbar.make(binding.root, "Months", Snackbar.LENGTH_LONG).show()
        }

        binding.radioBtnWeeks.setOnClickListener {
            Snackbar.make(binding.root, "Weeks", Snackbar.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun renderUi() {

        chartViewModel.chartPageObserveLiveData.observe(viewLifecycleOwner) { page ->
            binding.prevPage.isEnabled = page > 1
        }

        networkStatus.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                chartViewModel.setScreenState(ChartViewState.ViewContentMain)
            } else {
                chartViewModel.setScreenState(ChartViewState.NetworkError)
            }
        }

        chartViewModel.chartViewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ChartViewState.Error -> {
                    disableNavigationButtons(value = 0)
                    disableRadioButtons(value = false)
                    binding.txtNetworkStatus.text = state.error
                    binding.txtNetworkStatus.visibility = View.VISIBLE
                    binding.progBarChart.visibility = View.GONE
                }
                is ChartViewState.Loading -> {
                    disableNavigationButtons(value = 0)
                    disableRadioButtons(value = false)
                    binding.txtNetworkStatus.visibility = View.GONE
                    binding.progBarChart.visibility = View.VISIBLE
                }
                is ChartViewState.ViewContentMain -> {
                    disableNavigationButtons(value = 1)
                    disableRadioButtons(value = true)
                    binding.txtNetworkStatus.visibility = View.GONE
                    binding.progBarChart.visibility = View.GONE
                }
                is ChartViewState.NetworkError -> {
                    disableNavigationButtons(value = 0)
                    disableRadioButtons(value = false)
                    binding.txtNetworkStatus.visibility = View.VISIBLE
                    binding.progBarChart.visibility = View.GONE
                }
            }
        }

        chartViewModel.radioButtonCheckedLiveData.observe(viewLifecycleOwner) { radioModel ->
            when (radioModel.radioButton) {
                R.id.radioBtnYears -> {
                    binding.radioBtnYears.isChecked = true
                }
                R.id.radioBtnMonths -> {
                    binding.radioBtnMonths.isChecked = true
                }
                R.id.radioBtnWeeks -> {
                    binding.radioBtnWeeks.isChecked = true
                }
            }
        }

//        chartViewModel.stargazersLiveData.observe(viewLifecycleOwner){ list ->
//            if (list != null) {
//                compareYearsModel(list.stargazers_list)
//            }
//        }

        chartViewModel.barChartListLiveData.observe(viewLifecycleOwner) { barChartModelList ->
            initBarChart(barChartModelList)
        }
    }

    private fun disableNavigationButtons(value: Int) {
        when (value) {
            2 -> {
                binding.nextPage.isEnabled = true
                binding.prevPage.isEnabled = true
            }
            1 -> {
                binding.nextPage.isEnabled = true
                binding.prevPage.isEnabled = false
            }
            0 -> {
                binding.nextPage.isEnabled = false
                binding.prevPage.isEnabled = false
            }
        }
    }

    private fun disableRadioButtons(value: Boolean) {
        when (value) {
            true -> {
                binding.radioBtnYears.isEnabled = true
                binding.radioBtnMonths.isEnabled = true
                binding.radioBtnWeeks.isEnabled = true
            }
            else -> {
                binding.radioBtnYears.isEnabled = false
                binding.radioBtnMonths.isEnabled = false
                binding.radioBtnWeeks.isEnabled = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initBarChart(list: List<BarChartModel>) {

        barchartGraph = binding.barChart
        createBarChartData(list)
        barDataSet = BarDataSet(barEntryList, "")
        val barData = BarData(barDataSet)

        //Hide unnecessary labels if no data in AXis
        barData.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (value > 0) {
                    super.getFormattedValue(value)
                } else {
                    ""
                }
            }
        })

        barchartGraph.data = barData
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS, 250)
        barDataSet.valueTextSize = 14f
        barDataSet.valueTextColor = Color.BLACK
        barchartGraph.description.isEnabled = false
        barchartGraph.axisRight.isEnabled = false
        barchartGraph.isDragEnabled = false

        //Check size of list
//        if (barEntryList.size > 5){
            barchartGraph.setVisibleXRangeMaximum(13f)
//        } else {
//            barChart.setVisibleXRangeMaximum(barEntryList.size.toFloat())
//        }
        barchartGraph.animateY(1000)
        barchartGraph.animateX(1000)

        val xAxis: XAxis = barchartGraph.xAxis

        //set labels
        xAxis.valueFormatter = IndexAxisValueFormatter(barLabelList)
        xAxis.setCenterAxisLabels(false)
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = Color.BLACK
        xAxis.textSize = 14f

        val axisLeft: YAxis = barchartGraph.axisLeft
        axisLeft.granularity = 5f
        axisLeft.axisMinimum = 0f

        val axisRight: YAxis = barchartGraph.axisRight
        axisRight.granularity = 1f
        axisRight.axisMinimum = 0f
        barchartGraph.invalidate()

        barchartGraph.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                //getting index of selected bar
                val x = barchartGraph.data.getDataSetForEntry(e).getEntryIndex(e as BarEntry)
                val year = barLabelList[x]
                val userList = barEntryList[x].data

                if (userList is List<*>){
                    getViaPoints(userList)?.let {
                        detailsViewModel.setUserList(it)
                    }
                }
                val direction = ChartFragmentDirections.actionChartFragmentToDetailsFragment(timePeriod = year, amountUsers = barEntryList.size)
                findNavController().navigate(directions = direction)
            }
            override fun onNothingSelected() {}
        })
    }

    //Check BarChart data.object after click on line
    private fun getViaPoints(list: List<*>): List<PresentationStargazersListItem>? {
        list.forEach { if (it !is PresentationStargazersListItem) return null }
        return list.filterIsInstance<PresentationStargazersListItem>()
            .apply { if (size != list.size) return null }
    }

    private fun createBarChartData(list: List<BarChartModel>) {
        barEntryList.clear()
        barLabelList.clear()

        for (i in list.indices) {
            barLabelList.add(i, list[i].period.toString())
//            Log.d("info", "Size is ${list[i].userInfo.size}")
            barEntryList.add(
                BarEntry(
                    i.toFloat(),
                    list[i].userInfo.size.toFloat(),
                    list[i].userInfo
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}