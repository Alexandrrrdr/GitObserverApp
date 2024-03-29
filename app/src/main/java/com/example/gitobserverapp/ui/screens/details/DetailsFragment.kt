package com.example.gitobserverapp.ui.screens.details

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitobserverapp.R
import com.example.gitobserverapp.databinding.FragmentDetailsBinding
import com.example.gitobserverapp.ui.screens.base.BaseFragment
import com.example.gitobserverapp.ui.screens.details.model.DetailsUser
import moxy.presenter.InjectPresenter


class DetailsFragment : BaseFragment<FragmentDetailsBinding>(FragmentDetailsBinding::inflate), DetailsView {

    @InjectPresenter
    lateinit var detailsPresenter: DetailsPresenter

    private val detailsAdapter: DetailsAdapter by lazy {
        DetailsAdapter()
    }

    private val args: DetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val period = args.timePeriod
        val amount = args.amountUsers
        val userList: Array<DetailsUser> = args.list

        recyclerViewInit()
        detailsPresenter.showData(period = period, amountUsers = amount, arrayList = userList)
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

    override fun showList(list: List<DetailsUser>, period: String, amountUsers: Int) {
        detailsAdapter.differ.submitList(list)
        val txtPeriod =
            requireActivity().resources.getText(R.string.details_period).toString() + period
        binding.txtDetailsHeader.text = txtPeriod
        val txtAmountUsers = requireActivity().resources.getText(R.string.details_total_amount)
            .toString() + amountUsers.toString()
        binding.totalAmount.text = txtAmountUsers
    }
}