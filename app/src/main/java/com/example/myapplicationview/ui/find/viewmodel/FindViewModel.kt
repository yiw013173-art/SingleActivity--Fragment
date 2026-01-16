package com.example.myapplicationview.ui.find.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationview.core.network.model.UserDto
import com.example.myapplicationview.core.network.repository.FindRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FindViewModel : ViewModel() {
    private val _users = MutableLiveData<List<UserDto>>(emptyList())
    val users: LiveData<List<UserDto>> = _users

    private val allList = mutableListOf<UserDto>()

    private var currentPage = 1
    val pageSize = 10

    fun loadData(isRefresh: Boolean) {
        viewModelScope.launch {
            try {
                if (isRefresh) {
                    currentPage = 1
                } else {
                    currentPage++
                }
                val list = withContext(Dispatchers.IO) {
                    FindRepository.getUserInfoWithCache(pageSize, currentPage).results
                }
                if (isRefresh) {
                    allList.clear()
                }
                allList.addAll(list)
                _users.value = allList.toList()
                launch(Dispatchers.IO) {
                    FindRepository.preloadData(pageSize, currentPage + 1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _users.value = allList.toList()
            }
        }
    }
}