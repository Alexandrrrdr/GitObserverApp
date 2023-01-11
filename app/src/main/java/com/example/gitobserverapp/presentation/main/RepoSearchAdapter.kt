package com.example.gitobserverapp.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.gitobserverapp.data.network.model.repo.Item
import com.example.gitobserverapp.databinding.RecItemBinding

class RepoSearchAdapter(private val listener: Listener): RecyclerView.Adapter<RepoSearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = let { RecItemBinding.inflate(LayoutInflater.from(parent.context), parent, false) }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item = differ.currentList[position], listener = listener)
    }

    inner class ViewHolder(private val binding: RecItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item, listener: Listener){
            Glide.with(itemView)
                .load(item.owner.avatar_url)
                .into(binding.imgRepo)
            binding.txtRepoName.text = item.name
            binding.txtRepoOwner.text = item.owner.login
            binding.txtRepoRateCounter.text = item.stargazers_count.toString()
            itemView.setOnClickListener {
                listener.onClick(item = item)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    private val difUtil = object : DiffUtil.ItemCallback<Item>(){
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, difUtil)

    interface Listener{
        fun onClick(item: Item)
    }
}