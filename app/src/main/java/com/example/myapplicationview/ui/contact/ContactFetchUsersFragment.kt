package com.example.myapplicationview.ui.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.myapplicationview.core.base.BaseFragment
import com.example.myapplicationview.databinding.FragmentContactFetchUsersBinding
import com.example.myapplicationview.ui.contact.viewmodel.ContactFetchUsersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactFetchUsersFragment : BaseFragment<FragmentContactFetchUsersBinding>() {
    private val viewModel: ContactFetchUsersViewModel by viewModels()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentContactFetchUsersBinding {
        return FragmentContactFetchUsersBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.buttonRetryFetch.setOnClickListener { requestUsers() }
        viewModel.startIfNeeded(DEFAULT_COUNT, DEFAULT_PAGE)
    }

    private fun requestUsers() {
        viewModel.loadUsers(DEFAULT_COUNT, DEFAULT_PAGE)
    }

    companion object {
        private const val DEFAULT_COUNT = 10
        private const val DEFAULT_PAGE = 1
    }
}
