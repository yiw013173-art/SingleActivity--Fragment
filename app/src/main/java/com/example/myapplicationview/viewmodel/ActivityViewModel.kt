package com.example.myapplicationview.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ActivityViewModel: ViewModel() {
    private val _userFlow: MutableStateFlow<String> = MutableStateFlow("")
    val userFlow: StateFlow<String> get() = _userFlow

    fun setUserId(id: String){
        _userFlow.value = id
    }

    fun clearUserId(){
        _userFlow.value = ""
    }

    private val _flowVisibility: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val flowVisibility: StateFlow<Boolean> = _flowVisibility

    fun UpdataBtnState(){
        _flowVisibility.value = false
    }
}