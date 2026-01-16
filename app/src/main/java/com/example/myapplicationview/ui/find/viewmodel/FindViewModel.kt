package com.example.myapplicationview.ui.find.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationview.core.network.bean.NetResult
import com.example.myapplicationview.core.network.model.UserDto
import com.example.myapplicationview.core.network.repository.FindRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindViewModel @Inject constructor(
    private val repository: FindRepository
) : ViewModel() {
    private val _users = MutableLiveData<List<UserDto>>(emptyList())
    val users: LiveData<List<UserDto>> = _users

    private val allList = mutableListOf<UserDto>()

    private var currentPage = 1
    val pageSize = 10

    init {
        observeUsers()
    }

    private fun observeUsers() {
        viewModelScope.launch {
            repository.userFlow.collect { result ->
                when (result) {
                    is NetResult.Success -> {
                        val list = result.data.results
                        if (currentPage == 1) {
                            allList.clear()
                        }
                        allList.addAll(list)
                        _users.value = allList.toList()
                    }
                    is NetResult.Error -> {
                        _users.value = allList.toList()
                    }
                    null -> Unit
                }
            }
        }
    }

    fun loadData(isRefresh: Boolean) {
        viewModelScope.launch {
            try {
                if (isRefresh) {
                    currentPage = 1
                } else {
                    currentPage++
                }
                repository.refreshUsers(pageSize, currentPage)
            } catch (e: Exception) {
                e.printStackTrace()
                _users.value = allList.toList()
            }
        }
    }
}
