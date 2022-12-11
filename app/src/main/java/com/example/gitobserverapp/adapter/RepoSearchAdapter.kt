package com.example.gitobserverapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitobserverapp.repository.network.model.Item
import com.example.gitobserverapp.databinding.RecItemBinding

class RepoSearchAdapter(private val listener: Listener): RecyclerView.Adapter<RepoSearchAdapter.ViewHolder>() {

    private lateinit var binding: RecItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = let { RecItemBinding.inflate(LayoutInflater.from(parent.context), parent, false) }
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item = differ.currentList[position], listener = listener)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(item: Item, listener: Listener){
            Glide.with(itemView)
                .load(item.owner.avatar_url)
                .into(binding.imgRepo)
            binding.txtRepoName.text = item.full_name
            binding.txtRepoOwner.text = item.owner.login
            binding.txtRepoRateCounter.text = item.stargazers_count.toString()
            itemView.setOnClickListener {
                listener.onClick(item = item)
            }
        }
    }

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