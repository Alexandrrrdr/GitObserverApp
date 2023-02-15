package com.example.gitobserverapp.ui.screens.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitobserverapp.R
import com.example.gitobserverapp.databinding.FragmentDetailsBinding
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter


class DetailsFragment : MvpAppCompatFragment(), DetailsView {

    @InjectPresenter
    lateinit var detailsViewPresenter: DetailsViewPresenter

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val detailsAdapter: DetailsAdapter by lazy {
        DetailsAdapter()
    }

    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val period = args.timePeriod
        val amount = args.amountUsers
        val userList: Array<User> = args.list

        recyclerViewInit()
        detailsViewPresenter.showData(period = period, amountUsers = amount, arrayList = userList)
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

    override fun showList(list: List<User>, period: String, amountUsers: Int) {
        detailsAdapter.differ.submitList(list)
        val txtPeriod =
            requireActivity().resources.getText(R.string.details_period).toString() + period
        binding.txtDetailsHeader.text = txtPeriod
        val txtAmountUsers = requireActivity().resources.getText(R.string.details_total_amount)
            .toString() + amountUsers.toString()
        binding.totalAmount.text = txtAmountUsers
    }
}