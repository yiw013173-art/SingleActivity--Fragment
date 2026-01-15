package com.example.myapplicationview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationview.repository.FindRepository
import com.example.network.model.UserDto
import com.example.network.model.UserResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FindViewModel: ViewModel() {
    private val _userFlow: MutableStateFlow<UserResponse> = MutableStateFlow(UserResponse())
    val userFlow: StateFlow<UserResponse> = _userFlow

    private val allList = mutableListOf<UserDto>()

    var currentPage = 1
    val pageSize = 10

    fun getUserInfo(count:Int){
        viewModelScope.launch {
           _userFlow.value =  FindRepository.getUserInfo(count)
        }
    }

    fun loadDataIndex(isRefresh: Boolean){
        viewModelScope.launch {
            try {
                if (isRefresh) currentPage =1 else currentPage++
                val list = FindRepository.getUserInfoWithIndex(pageSize, currentPage).results
                if (isRefresh){
                    allList.clear()
                    allList.addAll(list)
                }else{
                    allList.addAll(list)
                }
                _userFlow.value = UserResponse(results = allList.toList())
            }catch (e:Exception){
                e.printStackTrace()
                _userFlow.value = UserResponse(results = allList.toList())
            }
        }
    }

    fun getPreloadData(isRefresh: Boolean){
        viewModelScope.launch {
            try {
                if (isRefresh) currentPage = 1 else currentPage++
                val list = FindRepository.getUserInfoWithCache(pageSize,currentPage).results
                if (isRefresh){
                    allList.clear()
                    allList.addAll(list)
                }else{
                    allList.addAll(list)
                }
                _userFlow.value = UserResponse(results = allList.toList())
                //预加载下一页数据
                launch {
                    FindRepository.preloadData(pageSize,currentPage +1)
                }
            }catch (e:Exception){
                e.printStackTrace()
                _userFlow.value = UserResponse(results = allList.toList())
        }
    }
        }

    fun loadData(isRefresh: Boolean) {
        if (isRefresh) currentPage =1 else currentPage++
        getUserInfo(pageSize * currentPage)
    }
}