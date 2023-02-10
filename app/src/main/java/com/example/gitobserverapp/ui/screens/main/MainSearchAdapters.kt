package com.example.gitobserverapp.ui.screens.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitobserverapp.data.remote.model.RemoteRepo
import com.example.gitobserverapp.databinding.RecItemBinding
import com.example.gitobserverapp.ui.screens.main.model.UiRepo

class MainSearchAdapters(private val listener: Listener): RecyclerView.Adapter<MainSearchAdapters.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = let { RecItemBinding.inflate(LayoutInflater.from(parent.context), parent, false) }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item = differ.currentList[position], listener = listener)
    }

    inner class ViewHolder(private val binding: RecItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UiRepo, listener: Listener){
            Glide.with(itemView)
                .load(item.owner.avatarUrl)
                .into(binding.imgRepo)
            binding.txtRepoName.text = item.name
            binding.txtRepoOwner.text = item.owner.login
            binding.txtRepoRateCounter.text = item.starsCount.toString()
            itemView.setOnClickListener {
                listener.onClick(item = item)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    private val difUtil = object : DiffUtil.ItemCallback<UiRepo>(){
        override fun areItemsTheSame(oldItem: UiRepo, newItem: UiRepo): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: UiRepo, newItem: UiRepo): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, difUtil)

    interface Listener{
        fun onClick(item: UiRepo)
    }
}