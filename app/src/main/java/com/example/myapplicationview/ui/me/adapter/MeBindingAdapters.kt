package com.example.myapplicationview.ui.me.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationview.core.network.model.MeData

@BindingAdapter("meItems")
fun bindMeItems(recyclerView: RecyclerView, items: List<MeData>?) {
    val adapter = recyclerView.adapter
    if (adapter is MeAdapter) {
        adapter.submitList(items.orEmpty())
    }
}
