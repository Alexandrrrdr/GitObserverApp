package com.example.gitobserverapp.ui.screens.barchart

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gitobserverapp.App
import com.example.gitobserverapp.R
import com.example.gitobserverapp.databinding.FragmentChartBinding
import com.example.gitobserverapp.domain.usecase.GetStarUseCase
import com.example.gitobserverapp.ui.screens.barchart.model.BarChartModel
import com.example.gitobserverapp.ui.screens.details.User
import com.example.gitobserverapp.utils.Constants.START_PAGE
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject


class ChartFragment:
    MvpAppCompatFragment(), ChartView, ChartHelper.Listener {

    @Inject lateinit var getStarGroupUseCase: GetStarUseCase

    private lateinit var chartHelper: ChartHelper
    @InjectPresenter
    lateinit var chartPresenter: ChartPresenter

    @ProvidePresenter
    fun provideChartViewPresenter(): ChartPresenter {
        return ChartPresenter(getStarGroupUseCase = getStarGroupUseCase)
    }

    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    //safeArgs from main fragment
    private val args: ChartFragmentArgs by navArgs()
    private var repoName = ""
    private var repoOwnerName = ""
    private var page = 1
    private var lastPage = 1

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

        chartHelper = ChartHelper(binding.barChart, this)
        repoName = args.repoName
        repoOwnerName = args.repoOwnerName
        binding.repoName.text = repoName
        navigationButtonsController(lastPage = lastPage, page = page)

        radioButtonClickListener()
        nextPageClick()
        prevPageClick()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun prevPageClick() {
        binding.prevPage.setOnClickListener {
            page++
            chartPresenter.prepareListForChart(page = page)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun nextPageClick() {
        binding.nextPage.setOnClickListener {
            page--
            chartPresenter.prepareListForChart(page = page)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun radioButtonClickListener() {
        binding.radioBtnYears.setOnClickListener {
            chartPresenter.getStargazersList(repoName = repoName, repoOwnerName = repoOwnerName)
        }
        binding.radioBtnMonths.setOnClickListener{
            Snackbar.make(binding.root, "Months", Snackbar.LENGTH_LONG).show()
        }
        binding.radioBtnWeeks.setOnClickListener {
            Snackbar.make(binding.root, "Weeks", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun navigationButtonsController(lastPage: Int, page: Int) {
        binding.nextPage.isEnabled = (page == lastPage && page != 1) || (page != 1 && page != lastPage)
        binding.prevPage.isEnabled = (page == 1 && lastPage > 1) || (page != 1 && page != lastPage)
    }

    override fun showLoadPage() {
        navigationButtonsController(START_PAGE, START_PAGE)
        binding.txtNetworkStatus.visibility = View.GONE
        binding.progBarChart.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun showSuccessPage(list: List<BarChartModel>, lastPage: Int, page: Int) {
        binding.txtNetworkStatus.visibility = View.GONE
        binding.progBarChart.visibility = View.GONE
        this.lastPage = lastPage
        this.page = page
        navigationButtonsController(lastPage = lastPage, page = page)
        chartHelper.initBarChart(list = list)
    }

    override fun showErrorPage(error: String) {
        navigationButtonsController(START_PAGE, START_PAGE)
        binding.txtNetworkStatus.text = error
        binding.txtNetworkStatus.visibility = View.VISIBLE
        binding.progBarChart.visibility = View.GONE
    }

    override fun showNetworkErrorPage() {
        navigationButtonsController(START_PAGE, START_PAGE)
        binding.txtNetworkStatus.visibility = View.VISIBLE
        binding.progBarChart.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun click(item: List<User>, year: String) {
        val arrayList: Array<User> = item.toTypedArray()
        val direction =
            ChartFragmentDirections.actionChartFragmentToDetailsFragment(
                timePeriod = year,
                amountUsers = arrayList.size,
                list = arrayList
            )
        findNavController().navigate(directions = direction)
    }

    override fun nothingClick() {
        Snackbar.make(binding.root, requireContext().getText(R.string.nothing_selected), Snackbar.LENGTH_SHORT).show()
    }
}