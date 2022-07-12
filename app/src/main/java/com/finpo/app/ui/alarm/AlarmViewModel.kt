package com.finpo.app.ui.alarm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.NotificationHistoryContent
import com.finpo.app.repository.NotificationRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.Paging
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.processor.internal.definecomponent.codegen._dagger_hilt_components_SingletonComponent
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    val paging: Paging<NotificationHistoryContent>
) : ViewModel() {
    private val _backEvent = MutableSingleLiveData<Boolean>()
    val backEvent: SingleLiveData<Boolean> = _backEvent

    private val _historyList = MutableLiveData<List<NotificationHistoryContent?>>()
    val historyList: LiveData<List<NotificationHistoryContent?>> = _historyList

    private val _isDeleteMode = MutableLiveData<Boolean>()
    val isDeleteMode: LiveData<Boolean> = _isDeleteMode

    private val _deleteBtnClickEvent = MutableSingleLiveData<Boolean>()
    val deleteBtnClickEvent: SingleLiveData<Boolean> = _deleteBtnClickEvent

    private val _deleteItemClickEvent = MutableSingleLiveData<Boolean>()
    val deleteItemClickEvent: SingleLiveData<Boolean> = _deleteItemClickEvent

    private val _alarmClickEvent = MutableSingleLiveData<NotificationHistoryContent>()
    val alarmClickEvent: SingleLiveData<NotificationHistoryContent> = _alarmClickEvent

    init {
        _isDeleteMode.value = false
        changeHistory()
    }

    fun alarmClick(data: NotificationHistoryContent) {
        _alarmClickEvent.setValue(data)
    }

    fun deleteItemClick(data: NotificationHistoryContent) {
        viewModelScope.launch {
            val deleteResponse = notificationRepository.deleteNotificationHistory(data.id)
            deleteResponse.onSuccess {
                val historyList = _historyList.value?.toMutableList() ?: mutableListOf()
                historyList.remove(data)
                _historyList.value = historyList
                paging.deleteData(data)
                _deleteItemClickEvent.setValue(true)
            }
        }
    }

    fun deleteClick() {
        _isDeleteMode.value = !_isDeleteMode.value!!
        _deleteBtnClickEvent.setValue(true)
    }

    fun refreshClick() {
        changeHistory()
    }

    fun backClick() {
        _backEvent.setValue(true)
    }

    private fun getLastId(): Int? {
        return try {
            var lastIdx = _historyList.value?.size ?: 0
            lastIdx -= 2 // 마지막 item은 null
            _historyList.value?.get(lastIdx)?.id
        } catch (e: Exception) { null }
    }

    private fun changeHistory() {
        paging.resetPage()

        viewModelScope.launch {
            val historyResponse = notificationRepository.getNotificationHistory(null)
            historyResponse.onSuccess {
                paging.loadData(
                    data.data.content.toMutableList(),
                    data.data.last, _historyList,
                    paging.changeData()
                )
                Log.d("noti","${_historyList.value}")
            }
        }
    }

    fun addHistory() {
        if(paging.isLastPage || paging.page.value == 0) return
        viewModelScope.launch {
            val historyResponse = notificationRepository.getNotificationHistory(getLastId())
            historyResponse.onSuccess {
                paging.loadData(
                    data.data.content.toMutableList(),
                    data.data.last, _historyList,
                    paging.addData()
                )
            }
        }
    }
}