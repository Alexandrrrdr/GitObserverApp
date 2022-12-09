package com.example.gitobserverapp.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitobserverapp.adapter.SearchAdapter
import com.example.gitobserverapp.api.RetrofitInstance
import com.example.gitobserverapp.api.ApiService
import com.example.gitobserverapp.api.model.GetRepos
import com.example.gitobserverapp.databinding.FragmentMainBinding
import com.example.gitobserverapp.utils.Constants.ORDER_BY
import com.example.gitobserverapp.utils.Constants.SORT_BY_RATE
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val searchAdapter: SearchAdapter by lazy {
        SearchAdapter()
    }
    private val apiService: ApiService by lazy {
        RetrofitInstance.getRetrofitClient().create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSearch.setOnClickListener {
            val searchWords: String = getText()
            binding.progBarMain.visibility = View.VISIBLE

            val getRepos = apiService.getRepos(searchWords, SORT_BY_RATE, ORDER_BY, 10)
            getRepos.enqueue(object : Callback<GetRepos>{
                override fun onResponse(call: Call<GetRepos>, response: Response<GetRepos>) {
                    binding.progBarMain.visibility = View.GONE
                    when(response.code()){
                        in 200..303->{
                            Log.d("mylog", "${response.code()}")
                            response.body()?.let { get_repos ->
                                get_repos.items.let { list ->
                                    searchAdapter.differ.submitList(list)
                                    binding.recView.layoutManager = LinearLayoutManager(requireContext())
                                    binding.recView.adapter = searchAdapter
                                }
                            }
                        }
                        in 304..421->{
                            Log.d("mylog", "${response.code()}")
                        }
                        in 422..502->{
                            Log.d("mylog", "${response.code()}")
                        }
                        in 503..599->{
                            Log.d("mylog", "${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<GetRepos>, t: Throwable) {
                    binding.progBarMain.visibility = View.GONE
                    Log.d("mylog", t.message.toString())
                }
            })
        }
    }
    private fun getText(): String {
        return binding.edtTxtInput.text.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}