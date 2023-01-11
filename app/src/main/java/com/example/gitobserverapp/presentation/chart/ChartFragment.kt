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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gitobserverapp.App
import com.example.gitobserverapp.R
import com.example.gitobserverapp.data.network.model.starred.User
import com.example.gitobserverapp.data.repository.ApiRepository
import com.example.gitobserverapp.databinding.FragmentChartBinding
import com.example.gitobserverapp.presentation.chart.chart_helper.ChartViewState
import com.example.gitobserverapp.presentation.chart.model.BarChartModel
import com.example.gitobserverapp.presentation.chart.model.UserModel
import com.example.gitobserverapp.presentation.details.DetailsViewModel
import com.example.gitobserverapp.presentation.details.model.UserData
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
    private var listUserModel = mutableListOf<UserModel>()

    //Start creating barCharts
    private lateinit var barDataSet: BarDataSet
    private lateinit var barChart: BarChart
    private var barEntryList = mutableListOf<BarEntry>()
    private var barLabelList = mutableListOf<String>()

    @Inject
    lateinit var apiRepository: ApiRepository
    @Inject
    lateinit var viewModel: ChartViewModel
    private val detailsViewModel: DetailsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkStatus = NetworkStatusHelper(requireContext())
    }

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

        radioButtonClick()
        renderUi()
        nextPageClick()
        prevPageClick()
    }

    private fun prevPageClick() {
        binding.prevPage.setOnClickListener {
            viewModel.setPageObserverLiveData(page - 1)
            page--
        }
    }

    private fun nextPageClick() {
        binding.nextPage.setOnClickListener {
            viewModel.setPageObserverLiveData(page + 1)
            page++
        }
    }

    private fun radioButtonClick() {
        binding.radioButtonGroup.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setCheckedRadioButton(isChecked)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun renderUi() {
        viewModel.chartPageObserveLiveData.observe(viewLifecycleOwner) { page ->
            binding.prevPage.isEnabled = page > 1
        }

        networkStatus.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                viewModel.setNetworkStatus(value = true)
            } else {
                viewModel.setNetworkStatus(value = false)
            }
        }

        viewModel.chartNetworkLiveData.observe(viewLifecycleOwner) { status ->
            if (status) viewModel.setScreenState(ChartViewState.ViewContentMain)
            else viewModel.setScreenState(ChartViewState.NetworkError)
        }

        viewModel.chartScreenState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ChartViewState.Error -> {
                    disableButtons(value = false)
                    binding.txtNetworkStatus.text = R.string.no_data_from_server.toString()
                    binding.txtNetworkStatus.visibility = View.VISIBLE
                    binding.progBarChart.visibility = View.GONE
                }
                is ChartViewState.Loading -> {
                    disableButtons(value = false)
                    binding.txtNetworkStatus.visibility = View.GONE
                    binding.progBarChart.visibility = View.VISIBLE
                }
                is ChartViewState.ViewContentMain -> {
                    disableButtons(value = true)
                    binding.txtNetworkStatus.visibility = View.GONE
                    binding.progBarChart.visibility = View.GONE
                }
                is ChartViewState.NetworkError -> {
                    disableButtons(value = false)
                    binding.txtNetworkStatus.visibility = View.VISIBLE
                    binding.progBarChart.visibility = View.GONE
                }
            }
        }

        viewModel.starredUsersLiveData.observe(viewLifecycleOwner) { userModel ->
            listUserModel.addAll(userModel)
//            compareYearsModel(let { userModel })
        }

        viewModel.radioButtonCheckedLiveData.observe(viewLifecycleOwner) { radioModel ->
            when (radioModel.radioButton) {
                R.id.radioBtnYears -> {
                    let {
                        viewModel.getDataFromGitHub(
                            repoOwnerName = repoOwnerName,
                            repoName = repoName,
                            createdAt = repoCreatedAt,
                            page = Constants.PAGE_START
                        )
                    }
                }
                R.id.radioBtnMonths -> {

                }
                R.id.radioBtnWeeks -> {

                }
            }
        }

        viewModel.barChartListLiveData.observe(viewLifecycleOwner) { barChartModelList ->
            initBarChart(barChartModelList)
        }
    }

    private fun disableButtons(value: Boolean) {
        when (value) {
            true -> {
                binding.radioBtnYears.isEnabled = true
                binding.radioBtnMonths.isEnabled = true
                binding.radioBtnWeeks.isEnabled = true
                binding.nextPage.isEnabled = true
            }
            else -> {
                binding.radioBtnYears.isEnabled = false
                binding.radioBtnMonths.isEnabled = false
                binding.radioBtnWeeks.isEnabled = false
                binding.nextPage.isEnabled = false
                binding.prevPage.isEnabled = false
            }
        }
    }

    private fun initBarChart(list: List<BarChartModel>) {
        barChart = binding.barChart
        createBarChartData(list)
        barDataSet = BarDataSet(barEntryList, "Test")
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

        barChart.data = barData
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS, 250)
        barDataSet.valueTextSize = 14f
        barDataSet.valueTextColor = Color.BLACK
        barChart.description.isEnabled = false
        barChart.axisRight.isEnabled = false
        barChart.isDragEnabled = false
        barChart.setVisibleXRangeMaximum(5f)
        barChart.animateY(1000)
        barChart.animateX(1000)

        val xAxis: XAxis = barChart.xAxis

        //set labels
        xAxis.valueFormatter = IndexAxisValueFormatter(barLabelList)
        xAxis.setCenterAxisLabels(false)
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE

        xAxis.textColor = Color.BLACK
        xAxis.textSize = 14f
        val axisLeft: YAxis = barChart.axisLeft
        axisLeft.granularity = 5f
        axisLeft.axisMinimum = 0f

        val axisRight: YAxis = barChart.axisRight
        axisRight.granularity = 1f
        axisRight.axisMinimum = 0f
        barChart.invalidate()
        barChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                //getting index of selected bar
                val x = barChart.data.getDataSetForEntry(e).getEntryIndex(e as BarEntry)
                val year = barLabelList[x]
                val amount = barEntryList[x].y.toInt()
                val userList: Any? = barEntryList[x].data
                val tmpList = mutableListOf<User>()
                val lister = userList to tmpList



//                val secondWay: List<User> = userList.filterIsInstance<User>()

                if (userList is List<*>){
                    getViaPoints(userList)?.let { UserData(year, it) }
                        ?.let { detailsViewModel.setUserList(it)
                            Log.d("info", "$it.")
                        }

                }
                //TODO it doesn't work


//                detailsViewModel.setUserList(UserData(year, tmpList))
//                val toDetails = ChartFragmentDirections.actionChartFragmentToDetailsFragment(period = year)
                findNavController().navigate(R.id.action_chartFragment_to_detailsFragment)
            }

            override fun onNothingSelected() {}
        })
    }

//    @Suppress("UNCHECKED_CAST")
//    inline fun <reified T : Any> List<*>.checkItemsAre() =
//        if (all { it is T })
//            this as List<T>
//        else null

    private fun getViaPoints(list: List<*>): List<User>? {

        list.forEach { if (it !is User) return null }

        return list.filterIsInstance<User>()
            .apply { if (size != list.size) return null }
    }

    private fun createBarChartData(list: List<BarChartModel>) {
        barEntryList.clear()
        for (i in list.indices) {
            barLabelList.add(i, list[i].period.toString())
            barEntryList.add(
                BarEntry(
                    i.toFloat(),
                    list[i].amount.toFloat(),
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