package com.example.myapplicationview.ui.contact.viewmodel

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplicationview.R
import com.example.myapplicationview.core.network.bean.NetResult
import com.example.myapplicationview.core.network.model.UserResponse
import com.example.myapplicationview.core.network.repository.FindRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactPreloadUsersViewModel @Inject constructor(
    application: Application,
    repository: FindRepository
) : AndroidViewModel(application) {

    private val app = getApplication<Application>()
    private val startTimeMs = SystemClock.elapsedRealtime()

    private val _statusText = MutableLiveData(app.getString(R.string.contact_status_waiting))
    val statusText: LiveData<String> = _statusText

    private val _elapsedText = MutableLiveData(app.getString(R.string.contact_elapsed_waiting))
    val elapsedText: LiveData<String> = _elapsedText

    init {
        viewModelScope.launch {
            repository.userFlow.collect { updateUiText(it) }
        }
    }

    private fun updateUiText(result: NetResult<UserResponse>?) {
        when (result) {
            null -> {
                _statusText.value = app.getString(R.string.contact_status_waiting)
                _elapsedText.value = app.getString(R.string.contact_elapsed_waiting)
            }
            is NetResult.Success -> {
                _statusText.value = app.getString(
                    R.string.contact_status_success,
                    result.data.results.size
                )
                val elapsed = SystemClock.elapsedRealtime() - startTimeMs
                _elapsedText.value = app.getString(R.string.contact_elapsed_time, elapsed)
            }
            is NetResult.Error -> {
                _statusText.value = app.getString(
                    R.string.contact_status_error,
                    result.exception.message ?: "unknown"
                )
                _elapsedText.value = app.getString(R.string.contact_elapsed_waiting)
            }
        }
    }
}
