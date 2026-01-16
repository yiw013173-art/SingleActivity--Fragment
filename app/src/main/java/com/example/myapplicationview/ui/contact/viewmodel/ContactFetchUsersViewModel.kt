package com.example.myapplicationview.ui.contact.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationview.core.network.bean.NetResult
import com.example.myapplicationview.core.network.model.UserResponse
import com.example.myapplicationview.core.network.repository.FindRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactFetchUsersViewModel @Inject constructor(
    private val repository: FindRepository
) : ViewModel() {

    private val _result = MutableStateFlow<NetResult<UserResponse>?>(null)
    val result: StateFlow<NetResult<UserResponse>?> = _result.asStateFlow()

    fun loadUsers(count: Int, page: Int) {
        viewModelScope.launch {
            _result.value = repository.fetchUsers(count, page)
        }
    }
}
