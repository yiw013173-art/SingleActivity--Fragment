package com.example.myapplicationview.fragment

import UserAdapter
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
import com.example.myapplicationview.databinding.FragmentFindBinding
import com.example.myapplicationview.viewmodel.FindViewModel
import com.scwang.smart.refresh.layout.constant.RefreshState
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private val findViewModel: FindViewModel by lazy {
    FindViewModel()
}

private lateinit var viewBinding: FragmentFindBinding

/**
 * A simple [Fragment] subclass.
 * Use the [FindFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FindFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val findViewModel: FindViewModel by viewModels()

    private  var _viewBinding: FragmentFindBinding? = null

    private val viewBinding get() = _viewBinding!!

    private val userAdapter by lazy {
        UserAdapter()
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
        // Inflate the layout for this fragment
        _viewBinding = FragmentFindBinding.inflate(inflater,container,false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupRefreshLayout()
        setupObservers()
        if (userAdapter.itemCount ==0) viewBinding.refreshLayoutFind.autoRefresh()
    }

    private fun setupRecyclerView(){
        viewBinding.recyclerViewFind.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
        }
    }

    private fun setupRefreshLayout(){
        viewBinding.refreshLayoutFind.setOnRefreshListener {
            findViewModel.loadData(true)
        }

        viewBinding.refreshLayoutFind.setOnLoadMoreListener {
            findViewModel.loadData(false)
        }
    }

    private fun setupObservers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                findViewModel.userFlow.collect {data->
                    userAdapter.submitList(data.results)
                    handleSmartRefreshState(data.results.size)
                }
            }
        }
    }

    private fun handleSmartRefreshState(size: Int){
        when(viewBinding.refreshLayoutFind.state){
            RefreshState.Refreshing->{
                viewBinding.refreshLayoutFind.finishRefresh()
            }

            RefreshState.Loading->{
                viewBinding.refreshLayoutFind.finishLoadMore()
            }

            else -> {}
        }
        if (size < findViewModel.pageSize){
            viewBinding.refreshLayoutFind.setNoMoreData(true)
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
         * @return A new instance of fragment FindFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FindFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}