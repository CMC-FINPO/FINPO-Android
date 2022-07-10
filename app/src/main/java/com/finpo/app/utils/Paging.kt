package com.finpo.app.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class Paging<T> @Inject constructor() {
    private val _itemList = MutableLiveData<MutableList<T?>>()
    private val itemList: LiveData<MutableList<T?>>
        get() = _itemList
    private val _page = MutableLiveData<Int>()
    val page: LiveData<Int>
        get() = _page

    var isLastPage = false

    init {
        _page.value = 0
        loading()
    }

    private fun loading() {
        _itemList.value = mutableListOf(null)
    }

    fun loadData(
        data: MutableList<T?>,
        isLastPage: Boolean,
        itemMutableLiveData: MutableLiveData<List<T?>>,
        updateFunction: (data: MutableList<T?>) -> Unit
    )
    {
        deleteLoading()
        addLoadingView(data, isLastPage)
        updateFunction(data)
        this.isLastPage = isLastPage
        itemMutableLiveData.value = itemList.value
        nextPage()
    }

    private fun deleteLoading() {
        if(_itemList.value?.isEmpty() == true)
            return
        if(_itemList.value?.last() == null) {
            _itemList.value?.removeLast()
            _itemList.value = _itemList.value
        }
    }

    fun deleteData(data: T) {
        _itemList.value?.remove(data)
        _itemList.value = _itemList.value
    }

    fun changeData(): (data: MutableList<T?>) -> Unit = { data ->
        _itemList.value = data
    }

    fun addData(): (data: MutableList<T?>) -> Unit = { data ->
        _itemList.value?.addAll(data)
        _itemList.value = _itemList.value
    }

    private fun nextPage() {
        _page.value = _page.value?.plus(1)
    }

    fun resetPage() {
        isLastPage = false
        _page.value = 0
    }

    private fun <T> addLoadingView(tmpEvaluationData: MutableList<T?>, isLastPage: Boolean) {
        if(!isLastPage) tmpEvaluationData.add(null)
    }
}