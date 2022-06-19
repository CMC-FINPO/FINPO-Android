package com.finpo.app.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.network.ApiService
import com.finpo.app.repository.SettingRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingRepository: SettingRepository
): ViewModel() {
    private val _logoutClickEvent = MutableSingleLiveData<Boolean>()
    val logoutClickEvent: SingleLiveData<Boolean> = _logoutClickEvent

    private val _withdrawalClickEvent = MutableSingleLiveData<Boolean>()
    val withdrawalClickEvent: SingleLiveData<Boolean> = _withdrawalClickEvent

    private val _withdrawalSuccessfulEvent = MutableSingleLiveData<Boolean>()
    val withdrawalSuccessfulEvent: SingleLiveData<Boolean> = _withdrawalSuccessfulEvent

    fun logoutClick() {
        _logoutClickEvent.setValue(true)
    }

    fun withdrawalClick() {
        _withdrawalClickEvent.setValue(true)
    }

    fun withdrawal() {
        viewModelScope.launch {
            val withdrawalResponse = settingRepository.withdrawal()
            _withdrawalSuccessfulEvent.setValue(withdrawalResponse is ApiResponse.Success)
        }
    }
}