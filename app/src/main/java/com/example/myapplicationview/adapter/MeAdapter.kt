package com.example.myapplicationview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationview.databinding.ItemMeBinding
import com.example.network.model.MeData

class MeAdapter: ListAdapter<MeData, MeAdapter.MeViewHolder>(MeDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeViewHolder {
        val binding = ItemMeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MeViewHolder, position: Int) {
        val meData = getItem(position)
        holder.bind(meData)
    }

    class MeViewHolder(private val binding: ItemMeBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item:MeData){
            binding.post = item
            binding.executePendingBindings()//强制立即刷新
        }
    }

    class MeDiffCallback: DiffUtil.ItemCallback<MeData>(){
        override fun areItemsTheSame(
            oldItem: MeData,
            newItem: MeData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MeData,
            newItem: MeData
        ): Boolean {
            return oldItem == newItem
        }

    }
}