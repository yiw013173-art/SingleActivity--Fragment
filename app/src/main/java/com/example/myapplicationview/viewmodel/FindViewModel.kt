package com.example.myapplicationview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.model.NetResult
import com.example.network.repository.FindRepository
import com.example.network.model.UserResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FindViewModel: ViewModel() {
    val _userFlow: MutableStateFlow<UserResponse> = MutableStateFlow(UserResponse())
    val userFlow: StateFlow<UserResponse> = _userFlow

    fun getUserInfo(count:Int){
        viewModelScope.launch {
            when(val result = FindRepository.getUserInfo(count)){
                is NetResult.Success -> {
                    _userFlow.value = result.data
                }
                is NetResult.Error -> {
                    _userFlow.value = UserResponse()
                }
            }
        }
    }
}