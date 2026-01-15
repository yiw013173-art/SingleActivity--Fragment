package com.example.myapplicationview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.model.MeData
import com.example.network.repository.MeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class MeViewModel: ViewModel() {
    private val _stateFlow: MutableStateFlow<List<MeData>> = MutableStateFlow(emptyList())

    val stateFlow: StateFlow<List<MeData>> = _stateFlow

    private val _finishLoadFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val finishLoadFlow: StateFlow<Boolean> = _finishLoadFlow

    private var allList = mutableListOf<MeData>()

    private var page: Int = 1

    private var limit: Int = 10

    private var isLoading = false // 防抖标志位

    fun loadData(isRefresh: Boolean){
        if (isLoading) return
        isLoading = true
        val targetPage = if (isRefresh)  1 else page+1
        viewModelScope.launch {
            try {
                val getList = withContext(Dispatchers.IO){
                    MeRepository.getMeDataCache(targetPage,limit)
                }
                if (isRefresh){
                    allList.clear()
                    page = 1
                }else{
                    if (getList.isNotEmpty()) page++
                }
                allList.addAll(getList)
                val finishData = getList.size < limit
                _finishLoadFlow.emit(finishData) // 如果获取的数据少于limit，说明没有更多数据了，通知ui更新状态
                _stateFlow.value = allList.toList()
            }catch (e:Exception){
                _stateFlow.value = allList.toList()
                e.printStackTrace()
            }finally {
                isLoading = false
            }
        }
    }
}