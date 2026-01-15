package com.example.myapplicationview.ui.contact

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.myapplicationview.core.base.BaseFragment
import com.example.myapplicationview.databinding.FragmentContactBinding

class ContactFragment : BaseFragment<FragmentContactBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentContactBinding {
        return FragmentContactBinding.inflate(inflater, container, false)
    }
}
