package com.example.myapplicationview.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplicationview.R
import com.example.myapplicationview.databinding.FragmentChat2Binding
import com.example.myapplicationview.databinding.FragmentChatBinding
import com.example.myapplicationview.model.People

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _viewBinding: FragmentChat2Binding? = null
    private val viewBinding get() = _viewBinding!!

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
        _viewBinding = FragmentChat2Binding.inflate(inflater,container,false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.pop.setOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.pushTo.setOnClickListener {
            var name: String = ""
            var age= viewBinding.edAge.text.toString().toIntOrNull()
            if (!viewBinding.edName.text.isNullOrEmpty()){
                name = viewBinding.edName.text.toString()
            }
//            if (age!=null && name.isNotEmpty()){
//                val people = People(name,age)
//                val action = ChatFragment2Directions.actionChatFragment2ToChatFragment3(people)
//                findNavController().navigate(action)
//            }else{
//                val action = ChatFragment2Directions.actionChatFragment2ToChatFragment3()
//                findNavController().navigate(action)
//            }
            val people2 = if (age!=null && name.isNotEmpty()){
                People(name,age)
            }else{
                null
            }
            val action2 = ChatFragment2Directions.actionChatFragment2ToChatFragment3(people2)
            findNavController().navigate(action2)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatFragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}