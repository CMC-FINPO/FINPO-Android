package com.finpo.app.ui.setting.interest_alarm_setting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.IdSubscribe
import com.finpo.app.model.remote.InterestCategory
import com.finpo.app.repository.NotificationRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterestAlarmSettingViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
): ViewModel() {
    private val _interestAlarmData = MutableLiveData<List<InterestCategory>>()
    val interestAlarmData: LiveData<List<InterestCategory>> = _interestAlarmData

    val allAlarmChecked = MutableLiveData<Boolean>()

    private val _backEvent = MutableSingleLiveData<Boolean>()
    val backEvent: SingleLiveData<Boolean> = _backEvent

    init {
        getMyNotification()
    }

    fun backClick() {
        _backEvent.setValue(true)
    }

    fun alarmCheckClick(id: Int, subscribe: Boolean) {
        viewModelScope.launch {
            val putNotificationResponse = notificationRepository.putMyNotification(interestSubscribe = listOf(IdSubscribe(id, !subscribe)))
            putNotificationResponse.onSuccess {
                setChecked(id, subscribe)
            }.onFailure {
                setChecked(id, !subscribe)
            }
            setAllAlarmChecked()
        }
    }

    private fun setChecked(id: Int, subscribe: Boolean) {
        _interestAlarmData.value?.forEach { if (it.id == id) it.subscribe = !subscribe }
        _interestAlarmData.value = _interestAlarmData.value
    }

    fun allAlarmCheckClick() {
        viewModelScope.launch {
            val tempAlarmData = mutableListOf<InterestCategory>()

            val interestIdSubscribeList = mutableListOf<IdSubscribe>()
            _interestAlarmData.value?.forEach {
                tempAlarmData.add(it)
                it.subscribe = allAlarmChecked.value ?: false
                interestIdSubscribeList.add(IdSubscribe(it.id, it.subscribe))
            }


            val allNotificationResponse =  notificationRepository.putMyNotification(interestSubscribe = interestIdSubscribeList)
            allNotificationResponse.onSuccess { _interestAlarmData.value = _interestAlarmData.value }
                .onFailure { _interestAlarmData.value = tempAlarmData }
        }
    }

    private fun setAllAlarmChecked() {
        allAlarmChecked.value = _interestAlarmData.value!!.count { !it.subscribe } == 0
    }

    private fun getMyNotification() {
        viewModelScope.launch {
            val myNotification = notificationRepository.getMyNotification()
            myNotification.onSuccess {
                _interestAlarmData.value = data.data.interestCategories ?: listOf()
                setAllAlarmChecked()
            }
        }
    }
}