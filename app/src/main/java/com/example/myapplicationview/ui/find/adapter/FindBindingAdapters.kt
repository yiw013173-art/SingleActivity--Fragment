package com.example.myapplicationview.ui.find.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.myapplicationview.R
import com.example.myapplicationview.core.network.model.UserDto
import com.example.myapplicationview.ui.find.UserAdapter

@BindingAdapter("users")
fun bindUsers(recyclerView: RecyclerView, users: List<UserDto>?) {
    val adapter = recyclerView.adapter
    if (adapter is UserAdapter) {
        adapter.submitList(users.orEmpty())
    }
}

@BindingAdapter("imageUrl")
fun bindImageUrl(imageView: ImageView, url: String?) {
    imageView.load(url) {
        crossfade(true)
        placeholder(R.drawable.ic_launcher_background)
        transformations(CircleCropTransformation())
    }
}
