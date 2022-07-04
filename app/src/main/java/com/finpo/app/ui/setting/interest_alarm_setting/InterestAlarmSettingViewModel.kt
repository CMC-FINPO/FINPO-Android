package com.finpo.app.ui.setting.interest_alarm_setting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.InterestCategory
import com.finpo.app.repository.NotificationRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
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

    fun allAlarmCheckClick() {
        _interestAlarmData.value?.forEach { it.subscribe = allAlarmChecked.value ?: false }
        _interestAlarmData.value = _interestAlarmData.value
    }

    private fun setAllAlarmChecked() {
        allAlarmChecked.value = _interestAlarmData.value!!.find { it.subscribe } != null
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