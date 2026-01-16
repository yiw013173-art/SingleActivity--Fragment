package com.example.myapplicationview.ui.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    val username = MutableLiveData("1")
    val password = MutableLiveData("1")

    private val _loginEvent = MutableLiveData<Event<Unit>>()
    val loginEvent: LiveData<Event<Unit>> = _loginEvent

    fun onLoginClicked() {
        val name = username.value.orEmpty().trim()
        val pass = password.value.orEmpty().trim()
        if (name.isNotEmpty() && pass.isNotEmpty()) {
            _loginEvent.value = Event(Unit)
        }
    }
}

class Event<out T>(private val content: T) {
    private var handled = false

    fun getContentIfNotHandled(): T? {
        if (handled) return null
        handled = true
        return content
    }
}
