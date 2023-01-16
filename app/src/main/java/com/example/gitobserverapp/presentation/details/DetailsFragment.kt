package com.example.gitobserverapp.presentation.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitobserverapp.R
import com.example.gitobserverapp.data.network.model.starred.User
import com.example.gitobserverapp.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

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
        recyclerViewInit()
        renderUi()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderUi() {
        detailsViewModel.usersList.observe(viewLifecycleOwner){ userData ->
            binding.txtDetailsHeader.text = "Period is ${userData.period}"
            detailsAdapter.differ.submitList(userData.users)
            binding.totalAmount.text = "Amount of users ${userData.users.size}"
            detailsAdapter.notifyDataSetChanged()
            Log.d("info", userData.users.size.toString())
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
 }