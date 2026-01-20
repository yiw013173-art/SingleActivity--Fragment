package com.example.myapplicationview.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplicationview.R
import com.example.myapplicationview.adapter.MeAdapter
import com.example.myapplicationview.databinding.FragmentMe2Binding
import com.example.myapplicationview.viewmodel.MeViewModel
import com.scwang.smart.refresh.layout.constant.RefreshState
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MeFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class MeFragment2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _viewBinding: FragmentMe2Binding? = null

    private val viewBinding get() = _viewBinding!!

    private val viewModel: MeViewModel by viewModels()

    private val meAdapter by lazy {
        MeAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentMe2Binding.inflate(inflater,container,false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupSmartLayout()
        setupObservers()
        if (meAdapter.itemCount == 0){
            viewBinding.smartMe2.autoRefresh()
//            viewModel.loadData(true)
        }
    }

    fun setupSmartLayout(){
        viewBinding.smartMe2.setOnRefreshListener {
            viewModel.loadData(true)
        }
        viewBinding.smartMe2.setOnLoadMoreListener {
            viewModel.loadData(false)
        }
    }

    fun setupAdapter(){
        viewBinding.recyclerMe.apply {
            adapter = meAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    fun setupObservers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    viewModel.stateFlow.collect {
                        meAdapter.submitList(it)
                        handleState(viewModel.statSize)
                    }
                }
                launch {
                    viewModel.finishLoadFlow.collect {
                        // 用于处理数据没有变化时，智能刷新状态无法结束的问题
                        handleState(20)
                    }
                }
            }
        }
    }

    fun handleState(size: Int){
        when(viewBinding.smartMe2.state){
            RefreshState.Refreshing->{
                viewBinding.smartMe2.finishRefresh()
            }
            RefreshState.Loading->{
                viewBinding.smartMe2.finishLoadMore()
            }
            else -> {}
        }
        if (size<viewModel.limit){
            viewBinding.smartMe2.setNoMoreData(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MeFragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MeFragment2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}