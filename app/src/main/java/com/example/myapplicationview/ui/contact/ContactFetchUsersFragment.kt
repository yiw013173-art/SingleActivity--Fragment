package com.example.myapplicationview.ui.contact

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplicationview.R
import com.example.myapplicationview.core.base.BaseFragment
import com.example.myapplicationview.core.network.bean.NetResult
import com.example.myapplicationview.core.network.model.UserResponse
import com.example.myapplicationview.databinding.FragmentContactFetchUsersBinding
import com.example.myapplicationview.ui.contact.viewmodel.ContactFetchUsersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactFetchUsersFragment : BaseFragment<FragmentContactFetchUsersBinding>() {
    private val viewModel: ContactFetchUsersViewModel by viewModels()
    private var startTimeMs: Long = 0L

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentContactFetchUsersBinding {
        return FragmentContactFetchUsersBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textStatus.text = getString(R.string.contact_status_loading)
        binding.textElapsed.text = getString(R.string.contact_elapsed_waiting)
        binding.buttonRetryFetch.setOnClickListener { requestUsers() }

        if (viewModel.result.value == null) {
            requestUsers()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.result.collect { result ->
                    binding.textStatus.text = buildStatusText(result)
                    binding.textElapsed.text = buildElapsedText(result)
                }
            }
        }
    }

    private fun requestUsers() {
        startTimeMs = SystemClock.elapsedRealtime()
        binding.textStatus.text = getString(R.string.contact_status_loading)
        binding.textElapsed.text = getString(R.string.contact_elapsed_waiting)
        viewModel.loadUsers(DEFAULT_COUNT, DEFAULT_PAGE)
    }

    private fun buildStatusText(result: NetResult<UserResponse>?): String {
        return when (result) {
            null -> getString(R.string.contact_status_loading)
            is NetResult.Success -> getString(R.string.contact_status_success, result.data.results.size)
            is NetResult.Error -> getString(
                R.string.contact_status_error,
                result.exception.message ?: "unknown"
            )
        }
    }

    private fun buildElapsedText(result: NetResult<UserResponse>?): String {
        return if (result is NetResult.Success) {
            val elapsed = SystemClock.elapsedRealtime() - startTimeMs
            getString(R.string.contact_elapsed_time, elapsed)
        } else {
            getString(R.string.contact_elapsed_waiting)
        }
    }

    companion object {
        private const val DEFAULT_COUNT = 10
        private const val DEFAULT_PAGE = 1
    }
}
