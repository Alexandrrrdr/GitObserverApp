package com.example.gitobserverapp.ui.screens.details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitobserverapp.R
import com.example.gitobserverapp.data.remote.model.RemoteStarGroup
import com.example.gitobserverapp.databinding.DetailsItemBinding

class DetailsAdapter(): RecyclerView.Adapter<DetailsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: DetailsItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(userData: RemoteStarGroup){
            binding.txtViewName.text = userData.users.name
            binding.txtViewUserId.text = userData.users.id.toString()
            Glide.with(itemView)
                .load(userData.users.userUrl)
                .placeholder(R.drawable.ic_image_placeholder)
                .into(binding.imgViewStar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = let { DetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false) }
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userData = differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    private val diffUtil = object : DiffUtil.ItemCallback<RemoteStarGroup>(){
        override fun areItemsTheSame(oldItem: RemoteStarGroup, newItem: RemoteStarGroup): Boolean {
            return oldItem.users.id == newItem.users.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: RemoteStarGroup, newItem: RemoteStarGroup): Boolean {
            return oldItem.users == newItem.users
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)
}