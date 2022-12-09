package com.example.gitobserverapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitobserverapp.R
import com.example.gitobserverapp.api.model.Item
import com.example.gitobserverapp.databinding.RecItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchAdapter(): RecyclerView.Adapter<SearchAdapter.SearchHolder>() {

    private lateinit var binding: RecItemBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        binding = RecItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return SearchHolder(binding.root)
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        val repoItem = differ.currentList[position]
        holder.bind(item = repoItem)
    }

    override fun getItemCount(): Int = differ.currentList.size

    private val diffUtils = object : DiffUtil.ItemCallback<Item>(){
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtils)


    inner class SearchHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(item: Item){
                Glide.with(context)
                    .load(item.owner.avatar_url)
                    .into(binding.imgRepo)
            binding.txtRepoName.text = item.name
            binding.txtRepoOwner.text = item.owner.login
            binding.txtRepoRateCounter.text = item.stargazers_count.toString()
        }
    }
}