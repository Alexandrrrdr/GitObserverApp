package com.example.gitobserverapp.ui.screens.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitobserverapp.databinding.FragmentDetailsBinding
import com.example.gitobserverapp.ui.screens.barchart.PresentationStargazersListItem
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter


class DetailsFragment : MvpAppCompatFragment(), DetailsView {

    @InjectPresenter
    lateinit var detailsViewPresenter: DetailsViewPresenter

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!


    private val args: DetailsFragmentArgs by navArgs()
    private var period = ""
    private var amount = 0

    private val detailsViewModel: DetailsViewModel by activityViewModels()
    private val detailsAdapter: DetailsAdapter by lazy {
        DetailsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        period = args.timePeriod
        amount = args.amountUsers

        recyclerViewInit()
        renderUi()
    }

    //TODO delete it after mvp pattern will work
    @SuppressLint("NotifyDataSetChanged")
    private fun renderUi() {
        detailsViewModel.usersList.observe(viewLifecycleOwner) { userData ->
            binding.txtDetailsHeader.text = "Period is $period"
            detailsAdapter.differ.submitList(userData)
            binding.totalAmount.text = "Amount of users $amount"
            detailsAdapter.notifyDataSetChanged()
        }
    }

    private fun recyclerViewInit() {
        binding.apply {
            recViewDetails.layoutManager = LinearLayoutManager(requireActivity())
            recViewDetails.adapter = detailsAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    //TODO Pass data from chart fragment
    override fun showList(list: List<PresentationStargazersListItem>, period: Int, amount: Int) {
        detailsAdapter.differ.submitList(list)
        binding.txtDetailsHeader.text = "Period is $period"
        binding.totalAmount.text = "Amount of users $amount"
    }
}