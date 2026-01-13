package com.example.myapplicationview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationview.repository.FindRepository
import com.example.network.model.UserResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FindViewModel: ViewModel() {
    val _userFlow: MutableStateFlow<UserResponse> = MutableStateFlow(UserResponse())
    val userFlow: StateFlow<UserResponse> = _userFlow

    var currentPage = 1
    private val pageSize = 10

    fun getUserInfo(count:Int){
        viewModelScope.launch {
           _userFlow.value =  FindRepository.getUserInfo(count)
        }
    }

    fun loadData(isRefresh: Boolean) {
        if (isRefresh) currentPage =1 else currentPage++
        getUserInfo(pageSize * currentPage)
    }
}