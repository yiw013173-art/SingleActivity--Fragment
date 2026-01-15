package com.example.myapplicationview.ui.find

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationview.core.network.model.UserDto
import com.example.myapplicationview.databinding.ItemUserBinding

/**
 * 适配器泛型传入 UserDto，因为这是列表中每一项的数据模型
 */
class UserAdapter : ListAdapter<UserDto, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        // 2. 获取当前位置的 UserDto 对象
        val user = getItem(position)
        holder.bind(user)
    }

    class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserDto) {
            binding.user = user
            binding.executePendingBindings()
        }
    }

    /**
     * DiffUtil 用于计算列表差异，避免全局刷新，提升性能
     */
    class UserDiffCallback : DiffUtil.ItemCallback<UserDto>() {
        // 使用唯一标识 uuid 来判断是否是同一个条目
        override fun areItemsTheSame(oldItem: UserDto, newItem: UserDto): Boolean {
            return oldItem.login.uuid == newItem.login.uuid
        }

        // 判断内容是否发生变化
        override fun areContentsTheSame(oldItem: UserDto, newItem: UserDto): Boolean {
            return oldItem == newItem
        }
    }
}
