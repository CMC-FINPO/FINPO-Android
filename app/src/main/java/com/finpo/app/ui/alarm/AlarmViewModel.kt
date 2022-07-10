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

    init {
        changeHistory()
    }

    fun refreshClick() {
        changeHistory()
    }

    fun backClick() {
        _backEvent.setValue(true)
    }

    fun changeHistory() {
        paging.resetPage()

        viewModelScope.launch {
            val historyResponse = notificationRepository.getNotificationHistory(paging.page.value ?: 0)
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

    fun addPolicy() {
        if(paging.isLastPage || paging.page.value == 0) return

        viewModelScope.launch {
            val historyResponse = notificationRepository.getNotificationHistory(paging.page.value ?: 0)
            historyResponse.onSuccess {
                paging.loadData(
                    data.data.content.toMutableList(),
                    data.data.last, _historyList,
                    paging.addData()
                )
                Log.d("noti","${_historyList.value}")
            }
        }
    }
}