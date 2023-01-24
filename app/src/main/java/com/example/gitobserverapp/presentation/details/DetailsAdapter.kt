package com.example.gitobserverapp.presentation.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitobserverapp.R
import com.example.gitobserverapp.databinding.DetailsItemBinding
import com.example.gitobserverapp.presentation.chart.model.PresentationChartListItem

class DetailsAdapter(): RecyclerView.Adapter<DetailsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: DetailsItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(userData: PresentationChartListItem){
            binding.txtViewName.text = userData.login
            binding.txtViewUserId.text = userData.id.toString()
            Glide.with(itemView)
                .load(userData.avatar_url)
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

    private val diffUtil = object : DiffUtil.ItemCallback<PresentationChartListItem>(){
        override fun areItemsTheSame(oldItem: PresentationChartListItem, newItem: PresentationChartListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PresentationChartListItem, newItem: PresentationChartListItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)
}