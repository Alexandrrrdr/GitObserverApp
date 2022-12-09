package com.example.gitobserverapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitobserverapp.api.model.GetRepos
import com.example.gitobserverapp.databinding.RecItemBinding

class RepoSearchAdapter(): RecyclerView.Adapter<RepoSearchAdapter.ViewHolder>() {

    private lateinit var binding: RecItemBinding
    private lateinit var context: Context
    private var repoList: MutableList<GetRepos.Item> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        binding = RecItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = repoList[position]
        holder.bind(item = item)
    }

    override fun getItemCount(): Int{
        Log.d("adapter", "${repoList.size}")
        return repoList.size
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(item: GetRepos.Item){
            Glide.with(context)
                .load(item.owner.avatar_url)
                .into(binding.imgRepo)
            binding.txtRepoName.text = item.name
            binding.txtRepoOwner.text = item.owner.login
            binding.txtLanguage.text = item.language
            binding.txtRepoRateCounter.text = item.stargazers_count.toString()
        }
    }

    fun fillRepoList(list: ArrayList<GetRepos.Item>){
        repoList.addAll(list)
    }
}