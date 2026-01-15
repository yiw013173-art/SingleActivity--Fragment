package com.example.myapplicationview.ui.me

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationview.core.network.model.MeData
import com.example.myapplicationview.core.network.repository.MeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MeViewModel : ViewModel() {
    private val _items = MutableLiveData<List<MeData>>(emptyList())
    val items: LiveData<List<MeData>> = _items

    private val _noMoreData = MutableLiveData(false)
    val noMoreData: LiveData<Boolean> = _noMoreData

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
                _noMoreData.value = finishData // 如果获取的数据少于limit，说明没有更多数据了，通知ui更新状态
                _items.value = allList.toList()
            }catch (e:Exception){
                _items.value = allList.toList()
                e.printStackTrace()
            }finally {
                isLoading = false
            }
        }
    }
}
