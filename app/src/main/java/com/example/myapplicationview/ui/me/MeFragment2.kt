package com.example.myapplicationview.ui.me

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.myapplicationview.core.base.BaseFragment
import com.example.myapplicationview.databinding.FragmentMe2Binding
import com.example.myapplicationview.ui.me.adapter.MeAdapter
import com.example.myapplicationview.ui.me.viewmodel.MeViewModel
import com.scwang.smart.refresh.layout.constant.RefreshState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeFragment2 : BaseFragment<FragmentMe2Binding>() {
    private val viewModel: MeViewModel by viewModels()
    private val adapter = MeAdapter()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMe2Binding {
        return FragmentMe2Binding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        setupRecyclerView()
        setupRefreshLayout()
        setupObservers()
        if (adapter.itemCount == 0) {
            binding.refreshLayoutMe.autoRefresh()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewMe.adapter = adapter
    }

    private fun setupRefreshLayout() {
        binding.refreshLayoutMe.setOnRefreshListener {
            viewModel.loadData(true)
        }
        binding.refreshLayoutMe.setOnLoadMoreListener {
            viewModel.loadData(false)
        }
    }

    private fun setupObservers() {
        viewModel.items.observe(viewLifecycleOwner) {
            handleSmartRefreshState()
        }
        viewModel.noMoreData.observe(viewLifecycleOwner) { noMore ->
            binding.refreshLayoutMe.setNoMoreData(noMore)
        }
    }

    private fun handleSmartRefreshState() {
        when (binding.refreshLayoutMe.state) {
            RefreshState.Refreshing -> binding.refreshLayoutMe.finishRefresh()
            RefreshState.Loading -> binding.refreshLayoutMe.finishLoadMore()
            else -> Unit
        }
    }
}
