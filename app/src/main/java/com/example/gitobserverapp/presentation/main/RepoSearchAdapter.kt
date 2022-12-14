package com.example.gitobserverapp.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitobserverapp.databinding.RecItemBinding

class RepoSearchAdapter(private val listener: Listener): RecyclerView.Adapter<RepoSearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = let { RecItemBinding.inflate(LayoutInflater.from(parent.context), parent, false) }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item = differ.currentList[position], listener = listener)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ViewHolder(private val binding: RecItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MainModel, listener: Listener){
            Glide.with(itemView)
                .load(item.repoImageUrl)
                .into(binding.imgRepo)
            binding.txtRepoName.text = item.repoName
            binding.txtRepoOwner.text = item.repoOwnerName
            binding.txtRepoRateCounter.text = item.repoStarAmount.toString()
            itemView.setOnClickListener {
                listener.onClick(item = item)
            }
        }
    }

    private val difUtil = object : DiffUtil.ItemCallback<MainModel>(){
        override fun areItemsTheSame(oldItem: MainModel, newItem: MainModel): Boolean {
            return oldItem.repoId == newItem.repoId
        }

        override fun areContentsTheSame(oldItem: MainModel, newItem: MainModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, difUtil)

    interface Listener{
        fun onClick(item: MainModel)
    }
}