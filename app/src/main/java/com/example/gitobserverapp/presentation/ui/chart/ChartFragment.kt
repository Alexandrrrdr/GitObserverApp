package com.example.gitobserverapp.presentation.ui.chart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.gitobserverapp.databinding.FragmentChartBinding
import com.google.android.material.snackbar.Snackbar

class ChartFragment : Fragment() {

    private var _binding: FragmentChartBinding?=null
    private val binding get() = _binding!!

    private val viewModel: ChartViewModel by activityViewModels()

   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repoOwnerLogin: String? = arguments?.getString("owner_name", "No owner name")
        val repoName: String? = arguments?.getString("repo_name", "No repo name")
        binding.repoName.text = "$repoName by $repoOwnerLogin"

        let { viewModel.getStarGazersData(repoOwnerName = repoOwnerLogin!!, repoName = repoName!!) }
        renderUi()

    }

    private fun renderUi(){
        viewModel.charData.observe(viewLifecycleOwner){ list ->
            Snackbar.make(binding.root, list[1].starredAt, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}