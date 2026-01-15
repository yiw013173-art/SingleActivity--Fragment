package com.example.myapplicationview.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.myapplicationview.core.base.BaseFragment
import com.example.myapplicationview.R
import com.example.myapplicationview.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val userName = binding.etUsername.text.toString()
            val passWord = binding.etPassword.text.toString()
            if (userName.isNotEmpty() && passWord.isNotEmpty()){
                // 使用 SafeArgs 生成的 action（nav_graph 中的 action_loginFragment_to_chat_graph）
                val action = LoginFragmentDirections.actionLoginFragmentToChatGraph()

                val option = NavOptions.Builder() // 启动模式
                    .setPopUpTo(R.id.loginFragment,true) // 跳转前先回到loginfragment，然后包括自己及上面的所有都删除
                    .setLaunchSingleTop(true) // 如果上面就是要跳转的页面，就不重复打开
                    .build()
                findNavController().navigate(action, option)
            }
        }
    }
}
