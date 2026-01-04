package com.example.myapplicationview.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.Visibility
import com.example.myapplicationview.R
import com.example.myapplicationview.databinding.FragmentChatBinding
import com.example.myapplicationview.viewmodel.ActivityViewModel
import com.example.myapplicationview.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _viewBinding: FragmentChatBinding? = null

    private val viewBinding get() = _viewBinding!!

//    private val args: ChatFragmentArgs by navArgs()

    private val userViewModel: ActivityViewModel by activityViewModels()

    private val chatViewModel: ChatViewModel by viewModels()

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
        _viewBinding = FragmentChatBinding.inflate(inflater,container,false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        if (!args.userid.isNullOrEmpty()){
//            userViewModel.setUserId(args.userid)
//        }
        viewBinding.btnChange.setOnClickListener {
            userViewModel.UpdataBtnState()
        }
        viewBinding.btnPush.setOnClickListener {
            val action = ChatFragmentDirections.actionNavChatToChatFragment2()
            findNavController().navigate(action)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    userViewModel.userFlow.collect {
                        viewBinding.tvNihk.text = it
                    }
                }
                launch {
                    userViewModel.flowVisibility.collect {
                        viewBinding.btnChange.visibility = if (it) View.VISIBLE else View.GONE
                    }
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}