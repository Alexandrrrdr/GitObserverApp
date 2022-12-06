package com.example.gitobserverapp.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gitobserverapp.databinding.RecItemBinding

class SearchAdapter: RecyclerView.Adapter<SearchAdapter.SearchHolder>() {

    private lateinit var binding: RecItemBinding

    inner class SearchHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}