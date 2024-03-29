package com.example.gitobserverapp.ui.screens.search

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gitobserverapp.databinding.RecItemBinding
import com.example.gitobserverapp.ui.screens.search.model.UiRepo
import com.example.gitobserverapp.utils.Extensions.convertToLocalDate

class SearchAdapter(private val clickListener: ClickListener) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    var adapterList = mutableListOf<UiRepo>()

    fun setListAdapter(list: List<UiRepo>?, isNewList: Boolean) {
        if (isNewList) {
            adapterList.clear()
            list?.let { adapterList.addAll(it) }
        } else {
            list?.let { adapterList.addAll(it) }
        }
        differ.submitList(adapterList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val binding =
            let { RecItemBinding.inflate(LayoutInflater.from(parent.context), parent, false) }
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        holder.bind(item = differ.currentList[position], clickListener = clickListener)
    }

    inner class ViewHolder(private val binding: RecItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: UiRepo, clickListener: ClickListener) {
            binding.repoName.text = item.name
            binding.repoDate.text = item.created.convertToLocalDate().toString()
            binding.repoStarAmount.text = item.starUserAmount.toString()
            itemView.setOnClickListener {
                clickListener.onClick(item = item)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val difUtil = object : DiffUtil.ItemCallback<UiRepo>() {
        override fun areItemsTheSame(oldItem: UiRepo, newItem: UiRepo): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: UiRepo, newItem: UiRepo): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, difUtil)

    interface ClickListener {
        fun onClick(item: UiRepo)
    }
}