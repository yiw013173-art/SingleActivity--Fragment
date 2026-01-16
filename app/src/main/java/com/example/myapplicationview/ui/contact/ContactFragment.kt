package com.example.myapplicationview.ui.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplicationview.R
import com.example.myapplicationview.core.base.BaseFragment
import com.example.myapplicationview.core.network.repository.FindRepository
import com.example.myapplicationview.databinding.FragmentContactBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactFragment : BaseFragment<FragmentContactBinding>() {
    @Inject
    lateinit var repository: FindRepository

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentContactBinding {
        return FragmentContactBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonFetchUsers.setOnClickListener {
            findNavController().navigate(R.id.action_nav_contact_to_contactFetchUsersFragment)
        }
        binding.buttonPreloadUsers.setOnClickListener {
            lifecycleScope.launch {
                repository.preloadUsers(DEFAULT_COUNT, DEFAULT_PAGE)
            }
            findNavController().navigate(R.id.action_nav_contact_to_contactPreloadUsersFragment)
        }
    }

    companion object {
        private const val DEFAULT_COUNT = 10
        private const val DEFAULT_PAGE = 1
    }
}
