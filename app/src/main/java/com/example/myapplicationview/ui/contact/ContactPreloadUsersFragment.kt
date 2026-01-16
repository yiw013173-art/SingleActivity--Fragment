package com.example.myapplicationview.ui.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.myapplicationview.core.base.BaseFragment
import com.example.myapplicationview.databinding.FragmentContactPreloadUsersBinding
import com.example.myapplicationview.ui.contact.viewmodel.ContactPreloadUsersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactPreloadUsersFragment : BaseFragment<FragmentContactPreloadUsersBinding>() {
    private val viewModel: ContactPreloadUsersViewModel by viewModels()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentContactPreloadUsersBinding {
        return FragmentContactPreloadUsersBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
    }
}
