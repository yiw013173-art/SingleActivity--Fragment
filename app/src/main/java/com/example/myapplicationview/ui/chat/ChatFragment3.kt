package com.example.myapplicationview.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplicationview.core.base.BaseFragment
import com.example.myapplicationview.data.local.dataStore
import com.example.myapplicationview.data.local.key
import com.example.myapplicationview.databinding.FragmentChat3Binding
import kotlinx.coroutines.launch

class ChatFragment3 : BaseFragment<FragmentChat3Binding>() {
    private val args: ChatFragment3Args by navArgs()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChat3Binding {
        return FragmentChat3Binding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.chatPeople?.let {
            binding.chat3Text.text = "${it.ages}------${it.names}"
        }
        binding.toPop.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                requireContext().dataStore.edit {
                    it[key] = 200
                }
            }
            findNavController().popBackStack()
        }

        lifecycleScope.launch {
            launch {
                requireContext().dataStore.data.collect {
                    binding.toPop.text = "读取到的count值为：${it[key] ?: 0}"
                }
            }
        }

    }

}
