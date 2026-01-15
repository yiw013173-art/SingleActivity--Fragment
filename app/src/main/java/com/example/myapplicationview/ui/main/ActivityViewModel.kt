package com.example.myapplicationview.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ActivityViewModel : ViewModel() {
    private val _userId = MutableLiveData("")
    val userId: LiveData<String> get() = _userId

    fun setUserId(id: String){
        _userId.value = id
    }

    fun clearUserId(){
        _userId.value = ""
    }

    private val _changeButtonVisible = MutableLiveData(true)
    val changeButtonVisible: LiveData<Boolean> get() = _changeButtonVisible

    fun hideChangeButton(){
        _changeButtonVisible.value = false
    }
}
