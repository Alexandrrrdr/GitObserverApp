package com.example.gitobserverapp.ui.screens.search

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gitobserverapp.databinding.RecItemBinding
import com.example.gitobserverapp.ui.screens.search.model.UiRepo
import com.example.gitobserverapp.utils.Constants
import com.example.gitobserverapp.utils.Extensions.convertToLocalDate

class SearchAdapter(private val listener: Listener): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var reposList = listOf<UiRepo>()
    private var isLoadAvailable = true
    private var usefulPage = "https://www.digitalocean.com/community/tutorials/android-recyclerview-load-more-endless-scrolling"

    fun setList(list: List<UiRepo>){
        this.reposList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = let { RecItemBinding.inflate(LayoutInflater.from(parent.context), parent, false) }
        return ViewHolder(binding)
//        if (viewType == Constants.VIEW_TYPE_ITEM){
//            val itemBinding = let { RecItemBinding.inflate(LayoutInflater.from(parent.context), parent, false) }
//
//        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item = differ.currentList[position], listener = listener)
    }

    inner class ViewHolder(private val binding: RecItemBinding): RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: UiRepo, listener: Listener){
            binding.repoName.text = item.name
            binding.repoDate.text = item.created.convertToLocalDate().toString()
            binding.repoStarAmount.text = item.starUserAmount.toString()
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