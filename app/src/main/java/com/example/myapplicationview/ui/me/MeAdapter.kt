package com.example.myapplicationview.ui.me

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationview.core.network.model.MeData
import com.example.myapplicationview.databinding.ItemMeBinding

class MeAdapter : ListAdapter<MeData, MeAdapter.MeViewHolder>(MeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMeBinding.inflate(inflater, parent, false)
        return MeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MeViewHolder(private val binding: ItemMeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MeData) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    class MeDiffCallback : DiffUtil.ItemCallback<MeData>() {
        override fun areItemsTheSame(oldItem: MeData, newItem: MeData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MeData, newItem: MeData): Boolean {
            return oldItem == newItem
        }
    }
}
