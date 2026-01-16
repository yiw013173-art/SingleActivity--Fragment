package com.example.myapplicationview.ui.me.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationview.core.network.bean.NetResult
import com.example.myapplicationview.core.network.model.MeData
import com.example.myapplicationview.core.network.repository.MeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeViewModel @Inject constructor(
    private val repository: MeRepository
) : ViewModel() {
    private val _items = MutableLiveData<List<MeData>>(emptyList())
    val items: LiveData<List<MeData>> = _items

    private val _noMoreData = MutableLiveData(false)
    val noMoreData: LiveData<Boolean> = _noMoreData

    private var allList = mutableListOf<MeData>()

    private var page: Int = 1

    private var limit: Int = 10

    private var isLoading = false // 防抖标志位
    private var requestedPage = 1

    init {
        observeMe()
    }

    private fun observeMe() {
        viewModelScope.launch {
            repository.meFlow.collect { result ->
                when (result) {
                    is NetResult.Success -> {
                        val getList = result.data
                        val isRefresh = requestedPage == 1
                        if (isRefresh) {
                            allList.clear()
                            page = 1
                        } else {
                            if (getList.isNotEmpty()) {
                                page++
                            }
                        }
                        allList.addAll(getList)
                        val finishData = getList.size < limit
                        _noMoreData.value = finishData
                        _items.value = allList.toList()
                    }
                    is NetResult.Error -> {
                        _items.value = allList.toList()
                    }
                    null -> Unit
                }
            }
        }
    }

    fun loadData(isRefresh: Boolean){
        if (isLoading) return
        isLoading = true
        val targetPage = if (isRefresh)  1 else page + 1
        requestedPage = targetPage
        viewModelScope.launch {
            try {
                repository.refreshMe(targetPage, limit)
            }catch (e:Exception){
                _items.value = allList.toList()
                e.printStackTrace()
            }finally {
                isLoading = false
            }
        }
    }
}
