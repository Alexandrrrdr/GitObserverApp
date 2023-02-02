package com.example.gitobserverapp.ui.screens.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitobserverapp.databinding.RecItemBinding

class MainSearchAdapters(private val listener: Listener): RecyclerView.Adapter<MainSearchAdapters.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = let { RecItemBinding.inflate(LayoutInflater.from(parent.context), parent, false) }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item = differ.currentList[position], listener = listener)
    }

    inner class ViewHolder(private val binding: RecItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RepoItem, listener: Listener){
            Glide.with(itemView)
                .load(item.owner_avatar_url)
                .into(binding.imgRepo)
            binding.txtRepoName.text = item.repo_name
            binding.txtRepoOwner.text = item.owner_login
            binding.txtRepoRateCounter.text = item.stargazers_count.toString()
            itemView.setOnClickListener {
                listener.onClick(item = item)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    private val difUtil = object : DiffUtil.ItemCallback<RepoItem>(){
        override fun areItemsTheSame(oldItem: RepoItem, newItem: RepoItem): Boolean {
            return oldItem.owner_id == newItem.owner_id
        }

        override fun areContentsTheSame(oldItem: RepoItem, newItem: RepoItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, difUtil)

    interface Listener{
        fun onClick(item: RepoItem)
    }
}