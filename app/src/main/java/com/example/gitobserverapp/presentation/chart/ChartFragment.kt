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
import com.example.gitobserverapp.utils.Constants
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
    private var lastPage = 1
    private var amountOfPagesForGraph = 0

    //    private lateinit var internet: InternetConnectionLiveData
    private lateinit var networkStatus: NetworkStatusHelper

    //Start creating barCharts
    private lateinit var barDataSet: BarDataSet
    private lateinit var barchartGraph: BarChart
    private var barEntryList = mutableListOf<BarEntry>()
    private var barLabelList = mutableListOf<String>()
    private var barChartListFromViewModel = mutableListOf<BarChartModel>()


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

        radioButtonClickListener()
        nextPageClick()
        prevPageClick()
        renderUi()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun prevPageClick() {
        binding.prevPage.setOnClickListener {
            chartViewModel.setPageObserverLiveData(page - 1)
            page--
            initBarChart(prepareListForChart(page = page, list = barChartListFromViewModel))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun nextPageClick() {
        binding.nextPage.setOnClickListener {
            chartViewModel.setPageObserverLiveData(page + 1)
            page++
            initBarChart(prepareListForChart(page = page, list = barChartListFromViewModel))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun radioButtonClickListener() {
        binding.radioBtnYears.setOnClickListener {
            chartViewModel.getStargazersList(Constants.START_PAGE)
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

            if (page == 1 && lastPage == 1){
                disableNavigationButtons(0)
            } else if (page == lastPage && page != 1) {
                disableNavigationButtons(3)
            } else if (page == 1){
                disableNavigationButtons(1)
            } else if (page != 1 && page != lastPage){
                disableNavigationButtons(2)
            }
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

        chartViewModel.barChartListLiveData.observe(viewLifecycleOwner) { barChartModelList ->
            if (barChartModelList.isEmpty()) disableNavigationButtons(0)
            setLastPage(barChartModelList)

            barChartListFromViewModel.addAll(barChartModelList)
            initBarChart(barChartModelList)
        }
    }

    private fun disableNavigationButtons(value: Int) {
        when (value) {
            3 -> {
                binding.nextPage.isEnabled = false
                binding.prevPage.isEnabled = true
            }
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

        //Showing depend on page number
        barchartGraph.setVisibleXRangeMaximum(5f)
        barchartGraph.setVisibleXRangeMinimum(5f)
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

                if (userList is List<*>) {
                    getViaPoints(userList)?.let { list ->
                        detailsViewModel.setUserList(list)
                        val direction =
                            ChartFragmentDirections.actionChartFragmentToDetailsFragment(
                                timePeriod = year,
                                amountUsers = userList.size
                            )
                        findNavController().navigate(directions = direction)
                    }
                }
            }
            override fun onNothingSelected() {}
        })
    }

    //Check BarChart data.object that it is List<PresentationStargazersListItem> after click on line
    private fun getViaPoints(list: List<*>): List<PresentationStargazersListItem>? {
        list.forEach { if (it !is PresentationStargazersListItem) return null }
        return list.filterIsInstance<PresentationStargazersListItem>()
            .apply { if (size != list.size) return null }
    }

    private fun createBarChartData(list: List<BarChartModel>) {
        barEntryList.clear()
        barLabelList.clear()
        setBarLists(list = list)
    }

    private fun setBarLists(list: List<BarChartModel>){
        for (i in list.indices) {
            barLabelList.add(i, list[i].period.toString())
            if (list[i].userInfo.isEmpty()){
                barEntryList.add(
                    BarEntry(
                        i.toFloat(),
                        list[i].userInfo.size.toFloat()
                    )
                )
            }
            barEntryList.add(
                BarEntry(
                    i.toFloat(),
                    list[i].userInfo.size.toFloat(),
                    list[i].userInfo
                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun prepareListForChart(page: Int, list: List<BarChartModel>): List<BarChartModel>{
        val tmpList = mutableListOf<BarChartModel>()

        when(page){
            1 -> {
                tmpList.clear()
                tmpList.addAll(list.slice(0..4))
                Log.d("info", "tmplist is ${tmpList.size}")
            }
            2 -> {
                tmpList.clear()
                tmpList.addAll(list.slice(5..9))
                Log.d("info", "${tmpList.size}")
            }
            3 -> {
                tmpList.clear()
                tmpList.addAll(list.slice(10..14))
                Log.d("info", "${tmpList.size}")
            }
            4 -> {
                tmpList.clear()
                tmpList.addAll(list.slice(15..19))
                Log.d("info", "${tmpList.size}")
            }
            5 -> {
                tmpList.clear()
                tmpList.addAll(list.slice(20..24))
                Log.d("info", "${tmpList.size}")
            }
            6 -> {
                tmpList.clear()
                tmpList.addAll(list.slice(25..29))
                Log.d("info", "page is $page last page is $lastPage")
            }
        }
        return tmpList
    }

    private fun setLastPage(list: List<BarChartModel>){
        chartViewModel.setPageObserverLiveData(1)
        val lastItems = list.size % 5
        lastPage = if (lastItems == 0) {
            list.size / 5
        } else {
            list.size / 5 + 1
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}