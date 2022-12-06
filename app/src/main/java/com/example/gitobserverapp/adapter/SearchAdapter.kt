package com.example.gitobserverapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gitobserverapp.databinding.RecItemBinding
import com.example.gitobserverapp.response.RepoListExample

class SearchAdapter(private val repoList: List<RepoListExample>): RecyclerView.Adapter<SearchAdapter.SearchHolder>() {

    inner class SearchHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(repoItem: RepoListExample){
            //TODO after will be created Model for response
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        val binding = RecItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchHolder(binding.root)
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        val repoItem = repoList[position]
        holder.bind(repoItem = repoItem)
    }

    override fun getItemCount(): Int = repoList.size
}