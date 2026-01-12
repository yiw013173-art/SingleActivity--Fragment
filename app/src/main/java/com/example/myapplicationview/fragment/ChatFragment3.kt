package com.example.myapplicationview.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplicationview.R
import com.example.myapplicationview.databinding.FragmentChat3Binding
import com.example.myapplicationview.util.dataStore
import com.example.myapplicationview.util.key
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment3.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment3 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _viewBinding: FragmentChat3Binding? = null

    private val viewBinding get() = _viewBinding!!

    private val args: ChatFragment3Args by navArgs()

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
        _viewBinding = FragmentChat3Binding.inflate(inflater,container,false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.chatPeople?.let {
            viewBinding.chat3Text.text = "${it.ages}------${it.names}"
        }
        viewBinding.toPop.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                requireContext().dataStore.edit {
                    it[key] = 200
                }
            }
            findNavController().popBackStack()
        }

        val count: Flow<Int> = requireContext().dataStore.data.map {
            it[key]?:0
        }

        lifecycleScope.launch {
            launch {
                requireContext().dataStore.data.collect {
                    viewBinding.toPop.text = "读取到的count值为：${it[key]?:0}"
                }
            }

//            launch {
//                requireContext().dataStore.edit {
//                    it[key] = 30
//                }
//            }

        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatFragment3.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment3().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}