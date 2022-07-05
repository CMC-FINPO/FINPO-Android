package com.finpo.app.ui.setting.region_alarm_setting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.IdSubscribe
import com.finpo.app.model.remote.InterestRegion
import com.finpo.app.repository.NotificationRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegionAlarmSettingViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
): ViewModel() {
    private val _regionAlarmData = MutableLiveData<List<InterestRegion>>()
    val regionAlarmData: LiveData<List<InterestRegion>> = _regionAlarmData

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
            val putNotificationResponse = notificationRepository.putMyNotification(regionSubscribe = listOf(IdSubscribe(id, !subscribe)))
            putNotificationResponse.onSuccess {
                setChecked(id, subscribe)
            }.onFailure {
                setChecked(id, !subscribe)
            }
            setAllAlarmChecked()
        }
    }

    private fun setChecked(id: Int, subscribe: Boolean) {
        _regionAlarmData.value?.forEach { if (it.id == id) it.subscribe = !subscribe }
        _regionAlarmData.value = _regionAlarmData.value
    }

    fun allAlarmCheckClick() {
        viewModelScope.launch {
            val tempAlarmData = mutableListOf<InterestRegion>()

            val regionIdSubscribeList = mutableListOf<IdSubscribe>()
            _regionAlarmData.value?.forEach {
                tempAlarmData.add(it)
                it.subscribe = allAlarmChecked.value ?: false
                regionIdSubscribeList.add(IdSubscribe(it.id, it.subscribe))
            }


            val allNotificationResponse =  notificationRepository.putMyNotification(regionSubscribe = regionIdSubscribeList)
            allNotificationResponse.onSuccess { _regionAlarmData.value = _regionAlarmData.value }
                .onFailure { _regionAlarmData.value = tempAlarmData }
        }
    }

    private fun setAllAlarmChecked() {
        allAlarmChecked.value = _regionAlarmData.value!!.count { !it.subscribe } == 0
    }

    private fun getMyNotification() {
        viewModelScope.launch {
            val myNotification = notificationRepository.getMyNotification()
            myNotification.onSuccess {
                _regionAlarmData.value = data.data.interestRegions ?: listOf()
                setAllAlarmChecked()
            }
        }
    }
}