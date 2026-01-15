package com.example.myapplicationview.ui.me

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplicationview.core.base.BaseFragment
import com.example.myapplicationview.databinding.FragmentMeBinding

class MeFragment : BaseFragment<FragmentMeBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMeBinding {
        return FragmentMeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnPush.setOnClickListener {
            val action = MeFragmentDirections.actionNavMeToMeFragment2()
            findNavController().navigate(action)
        }
    }

}
