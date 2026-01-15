package com.example.myapplicationview.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplicationview.core.base.BaseFragment
import com.example.myapplicationview.data.model.People
import com.example.myapplicationview.databinding.FragmentChat2Binding

class ChatFragment2 : BaseFragment<FragmentChat2Binding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChat2Binding {
        return FragmentChat2Binding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pop.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.pushTo.setOnClickListener {
            val name = binding.edName.text?.toString().orEmpty()
            val age = binding.edAge.text.toString().toIntOrNull()
            val people = if (age != null && name.isNotEmpty()) {
                People(name, age)
            } else {
                null
            }
            val action = ChatFragment2Directions.actionChatFragment2ToChatFragment3(people)
            findNavController().navigate(action)
        }
    }
}
