package com.example.myapplicationview.ui.find.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.myapplicationview.core.base.BaseFragment
import com.example.myapplicationview.databinding.FragmentFindBinding
import com.example.myapplicationview.ui.find.viewmodel.FindViewModel
import com.example.myapplicationview.ui.find.UserAdapter
import com.scwang.smart.refresh.layout.constant.RefreshState

class FindFragment : BaseFragment<FragmentFindBinding>() {
    private val findViewModel: FindViewModel by viewModels()

    private val userAdapter by lazy {
        UserAdapter()
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFindBinding {
        return FragmentFindBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = findViewModel
        setupRecyclerView()
        setupRefreshLayout()
        setupObservers()
        if (userAdapter.itemCount == 0) {
            binding.refreshLayoutFind.autoRefresh()
        }
    }

    private fun setupRecyclerView(){
        binding.recyclerViewFind.apply {
            adapter = userAdapter
        }
    }

    private fun setupRefreshLayout(){
        binding.refreshLayoutFind.setOnRefreshListener {
            findViewModel.loadData(true)
        }

        binding.refreshLayoutFind.setOnLoadMoreListener {
            findViewModel.loadData(false)
        }
    }

    private fun setupObservers(){
        findViewModel.users.observe(viewLifecycleOwner) { data ->
            handleSmartRefreshState(data.size)
        }
    }

    private fun handleSmartRefreshState(size: Int){
        when(binding.refreshLayoutFind.state){
            RefreshState.Refreshing->{
                binding.refreshLayoutFind.finishRefresh()
            }

            RefreshState.Loading->{
                binding.refreshLayoutFind.finishLoadMore()
            }

            else -> {}
        }
        binding.refreshLayoutFind.setNoMoreData(size < findViewModel.pageSize)
    }
}