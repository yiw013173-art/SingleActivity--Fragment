package com.example.myapplicationview.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.myapplicationview.MainActivity
import com.example.myapplicationview.R
import com.example.myapplicationview.databinding.FragmentBlankBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _viewBinding: FragmentBlankBinding? = null
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

        _viewBinding = FragmentBlankBinding.inflate(inflater,container,false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.btnBack.setOnClickListener {
            Toast.makeText(requireActivity(), "我返回了", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            val options = NavOptions.Builder()
                .setLaunchSingleTop(true)
//                .setPopUpTo(R.id.homeFragment,true)
//                .build()
//            findNavController().navigate(R.id.action_blankFragment_to_homeFragment,null,options)
            //清空homeFragment（包括自己）以上的所有fragment，并且跳转到homeFragment
//            findNavController().navigate(R.id.action_blankFragment_to_homeFragment){
//                popUpTo(R.id.homeFragment){
//                    inclusive = true
//                }
//                launchSingleTop = true
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
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlankFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}