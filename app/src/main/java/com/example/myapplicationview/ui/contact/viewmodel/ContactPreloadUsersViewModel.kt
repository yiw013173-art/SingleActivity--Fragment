package com.example.myapplicationview.ui.contact.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplicationview.core.network.bean.NetResult
import com.example.myapplicationview.core.network.model.UserResponse
import com.example.myapplicationview.core.network.repository.FindRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ContactPreloadUsersViewModel @Inject constructor(
    repository: FindRepository
) : ViewModel() {
    val userFlow: StateFlow<NetResult<UserResponse>?> = repository.userFlow
}
