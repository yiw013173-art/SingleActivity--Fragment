package com.example.myapplicationview.fragment

import android.content.Context
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplicationview.MainActivity
import com.example.myapplicationview.R
import com.example.myapplicationview.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private val TAG = "HomeFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _viewBinding: FragmentHomeBinding? = null
    private val viewBinding get() = _viewBinding!!

//    private val args: HomeFragmentArgs by navArgs()

    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = requireActivity() as MainActivity //一定要拿到
        Log.d(TAG, "onAttach: activity和Fragment首次关联")//可以在这里初始化关联activity的一些操作
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Fragment实例被创建")//加载本地数据和viewmodel
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
        Log.d(TAG, "onCreateView: Fragment视图被创建")//加载布局，绑定控件
        _viewBinding = FragmentHomeBinding.inflate(inflater,container,false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        _viewBinding = FragmentHomeBinding.bind(view)
        val userName = arguments?.getString("userName")
        val passWord = arguments?.getString("passWord")
//        val userName2 = args.userId
//        val passWord2 = args.password

        var data: String = ""
        if (!userName.isNullOrEmpty()){
            data = data+userName
        }
        if (!passWord.isNullOrEmpty()){
            data = data+"-----$passWord"
        }
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                AlertDialog.Builder(requireContext())
                    .setTitle("温馨提示")
                    .setMessage("确定要关闭吗？")
                    .setPositiveButton("确定",{log,_->
//                        isEnabled = false
//                        requireActivity().onBackPressedDispatcher.onBackPressed()
//                        requireActivity().finish()
                        log.dismiss()
                        requireActivity().moveTaskToBack(true) // 将应用退到后台，但不是销毁
                    })
                    .setNegativeButton("取消",null)
                    .show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)
        viewBinding.textInput.text = data
        viewBinding.textInput.text = "你好===你好"
        viewBinding.btnTo.setOnClickListener {
           requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        Log.d(TAG, "onViewCreated: 视图加载完成后")//这里可以做一些点击事件什么的操作
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d(TAG, "onViewStateRestored: 视图恢复创建后调用")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: 页面可见")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: 页面可交互，活跃状态")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: 页面失去焦点")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: 处于后台")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: 视图被销毁，但fragment实例还在")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: fragment实例被销毁")//释放资源
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach: Fragment和activity取消关联")
    }




    companion object { // 伴身对象，里面写的就是类似java的静态成员
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic // 加了这个注解后，java可以直接类名.方法名调用，
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}