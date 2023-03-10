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
import com.example.gitobserverapp.databinding.RecItemLoadingBinding
import com.example.gitobserverapp.ui.screens.search.model.UiRepo
import com.example.gitobserverapp.utils.Constants.VIEW_TYPE_ITEM
import com.example.gitobserverapp.utils.Constants.VIEW_TYPE_LOADING
import com.example.gitobserverapp.utils.Extensions.convertToLocalDate

class SearchAdapter(private val clickListener: ClickListener): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    var adapterList = mutableListOf<UiRepo>()

    fun setListAdapter(list: List<UiRepo>?, isNewList: Boolean){
        if (isNewList){
            adapterList.clear()
            list?.let { adapterList.addAll(it) }
        } else {
            list?.let { adapterList.addAll(it) }
        }
        differ.submitList(adapterList)
    }

//    override fun getItemViewType(position: Int): Int {
//        if (adapterList[position] == null){
//            return VIEW_TYPE_LOADING
//        } else {
//            return VIEW_TYPE_ITEM
//        }
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
//        if (viewType == VIEW_TYPE_ITEM) {
            val binding =
                let { RecItemBinding.inflate(LayoutInflater.from(parent.context), parent, false) }
            return ViewHolder(binding)
//        } else {
//            val loadBinding = let { RecItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false) }
//            return LoadingViewHolder(loadBinding = loadBinding)
//        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        holder.bind(item = differ.currentList[position], clickListener = clickListener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fillAdapter(viewHolder: ViewHolder, position: Int){
        viewHolder.bind(item = differ.currentList[position], clickListener = clickListener)
    }

    inner class LoadingViewHolder(private val loadBinding: RecItemLoadingBinding): RecyclerView.ViewHolder(loadBinding.root){
    }

    inner class ViewHolder(private val binding: RecItemBinding): RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: UiRepo, clickListener: ClickListener){
            binding.repoName.text = item.name
            binding.repoDate.text = item.created.convertToLocalDate().toString()
            binding.repoStarAmount.text = item.starUserAmount.toString()
            itemView.setOnClickListener {
                clickListener.onClick(item = item)
            }
        }
    }

    override fun getItemCount(): Int {
//        if (adapterList == null){
//            return 0
//        } else {
            return differ.currentList.size
//        }
    }

    private val difUtil = object : DiffUtil.ItemCallback<UiRepo>(){
        override fun areItemsTheSame(oldItem: UiRepo, newItem: UiRepo): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: UiRepo, newItem: UiRepo): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, difUtil)

    interface ClickListener{
        fun onClick(item: UiRepo)
    }
}