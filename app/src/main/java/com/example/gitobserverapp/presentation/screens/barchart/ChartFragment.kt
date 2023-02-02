package com.example.gitobserverapp.presentation.screens.barchart

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gitobserverapp.App
import com.example.gitobserverapp.databinding.FragmentChartBinding
import com.example.gitobserverapp.domain.usecase.GetStargazersUseCase
import com.example.gitobserverapp.presentation.screens.details.DetailsViewModel
import com.example.gitobserverapp.utils.ExtensionHideKeyboard
import com.example.gitobserverapp.utils.ExtensionHideKeyboard.hideKeyboard
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
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject


class ChartFragment():
    MvpAppCompatFragment(), ChartView {

    @Inject lateinit var getStargazersUseCase: GetStargazersUseCase

    @InjectPresenter
    lateinit var chartViewPresenter: ChartViewPresenter

    @ProvidePresenter
    fun provideChartViewPresenter(): ChartViewPresenter {
        return ChartViewPresenter(getStargazersUseCase = getStargazersUseCase)
    }

    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    //safeArgs from main fragment
    private val args: ChartFragmentArgs by navArgs()
    private var repoName = ""
    private var repoOwnerName = ""
    private var repoCreatedAt = ""
    private var page = 1
    private var lastPage = 1

    //Start creating barCharts
    private lateinit var barDataSet: BarDataSet
    private lateinit var barchartGraph: BarChart
    private var barEntryList = mutableListOf<BarEntry>()
    private var barLabelList = mutableListOf<String>()

    //TODO change LiveData to something other. For example Dagger implementation some class for change by data
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
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repoName = args.repoName
        repoOwnerName = args.repoOwnerName
        repoCreatedAt = args.repoCreatedAt
        binding.repoName.text = repoName
        navigationButtonsController(lastPage = lastPage, page = page)

        radioButtonClickListener()
        nextPageClick()
        prevPageClick()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun prevPageClick() {
        binding.prevPage.setOnClickListener {
            page--
            chartViewPresenter.prepareListForChart(page = page)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun nextPageClick() {
        binding.nextPage.setOnClickListener {
            page++
            chartViewPresenter.prepareListForChart(page = page)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun radioButtonClickListener() {
        binding.radioBtnYears.setOnClickListener {
            chartViewPresenter.getStargazersList(repoName = repoName, repoOwnerName = repoOwnerName)
        }
        binding.radioBtnMonths.setOnClickListener{
            Snackbar.make(binding.root, "Months", Snackbar.LENGTH_LONG).show()
        }
        binding.radioBtnWeeks.setOnClickListener {
            Snackbar.make(binding.root, "Weeks", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun navigationButtonsController(lastPage: Int, page: Int) {
            if (page == 1 && lastPage == 1) {
                disableNavigationButtons(0)
            } else if (page == lastPage && page != 1) {
                disableNavigationButtons(3)
            } else if (page == 1 && lastPage > 1) {
                disableNavigationButtons(1)
            } else if (page != 1 && page != lastPage) {
                disableNavigationButtons(2)
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

    override fun showLoadPage() {
        disableNavigationButtons(value = 0)
        disableRadioButtons(value = false)
        binding.txtNetworkStatus.visibility = View.GONE
        binding.progBarChart.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun showSuccessPage(list: List<BarChartModel>, lastPage: Int, page: Int) {
//        disableNavigationButtons(value = 1)
        disableRadioButtons(value = true)
        binding.txtNetworkStatus.visibility = View.GONE
        binding.progBarChart.visibility = View.GONE
        this.lastPage = lastPage
        this.page = page
        navigationButtonsController(lastPage = lastPage, page = page)
        initBarChart(list = list)
    }

    override fun showErrorPage(error: String) {
        disableNavigationButtons(value = 0)
        disableRadioButtons(value = false)
        binding.txtNetworkStatus.text = error
        binding.txtNetworkStatus.visibility = View.VISIBLE
        binding.progBarChart.visibility = View.GONE
    }

    override fun showNetworkErrorPage() {
        disableNavigationButtons(value = 0)
        disableRadioButtons(value = false)
        binding.txtNetworkStatus.visibility = View.VISIBLE
        binding.progBarChart.visibility = View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initBarChart(list: List<BarChartModel>) {

        barEntryList.addAll(emptyList())
        barLabelList.addAll(emptyList())
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
        axisLeft.granularity = 1f
        axisLeft.axisMinimum = 0f

        barchartGraph.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                //getting index of selected bar
//                val x = barchartGraph.data.getDataSetForEntry(e).getEntryIndex(e as BarEntry)
                val x = e!!.x.toInt()
                val year = barLabelList[x]
                val userList: Any? = barEntryList[x].data

                if (userList is List<*>){
                    getViaPoints(userList)?.let { list ->
                        if (list.isNotEmpty()) {
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
            }
            override fun onNothingSelected() {
                Snackbar.make(binding.root, "Field is Empty", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    //Check BarChart data.object after click on line
    private fun getViaPoints(list: List<*>): List<PresentationStargazersListItem>? {
        list.forEach { if (it !is PresentationStargazersListItem) return null }
        return list.filterIsInstance<PresentationStargazersListItem>()
            .apply { if (size != list.size) return null }
    }

    private fun createBarChartData(list: List<BarChartModel>) {

        for (i in list.indices) {
            barLabelList.add(i, list[i].period.toString())
            if (list[i].userInfo.isEmpty()){
                barEntryList.add(
                    BarEntry(
                        i.toFloat(),
                        list[i].userInfo.size.toFloat()
                    )
                )
            } else {
                barEntryList.add(
                    BarEntry(
                        i.toFloat(),
                        list[i].userInfo.size.toFloat(),
                        list[i].userInfo
                    )
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}