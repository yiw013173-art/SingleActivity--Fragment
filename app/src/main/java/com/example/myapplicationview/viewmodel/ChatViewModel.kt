package com.example.myapplicationview.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatViewModel: ViewModel() {
    private val _flowVisibility: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val flowVisibility: StateFlow<Boolean> = _flowVisibility

    fun UpdataBtnState(){
        _flowVisibility.value = false
    }
}