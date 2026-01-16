package com.example.myapplicationview.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplicationview.core.base.BaseFragment
import com.example.myapplicationview.databinding.FragmentChatBinding
import com.example.myapplicationview.ui.main.viewmodel.ActivityViewModel

class ChatFragment : BaseFragment<FragmentChatBinding>() {
    private val userViewModel: ActivityViewModel by activityViewModels()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatBinding {
        return FragmentChatBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = userViewModel
        binding.btnPush.setOnClickListener {
            val action = ChatFragmentDirections.actionNavChatToChatFragment2()
            findNavController().navigate(action)
        }
    }
}
