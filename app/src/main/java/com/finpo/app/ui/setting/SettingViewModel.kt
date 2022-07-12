package com.finpo.app.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.GoogleToken
import com.finpo.app.network.ApiService
import com.finpo.app.repository.GoogleLoginRepository
import com.finpo.app.repository.SettingRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
    private val googleApiRepository: GoogleLoginRepository
): ViewModel() {
    private val _logoutClickEvent = MutableSingleLiveData<Boolean>()
    val logoutClickEvent: SingleLiveData<Boolean> = _logoutClickEvent

    private val _withdrawalClickEvent = MutableSingleLiveData<Boolean>()
    val withdrawalClickEvent: SingleLiveData<Boolean> = _withdrawalClickEvent

    private val _withdrawalSuccessfulEvent = MutableSingleLiveData<Int?>()
    val withdrawalSuccessfulEvent: SingleLiveData<Int?> = _withdrawalSuccessfulEvent

    private val _goToInterestAlarmSettingFragmentEvent = MutableSingleLiveData<Boolean>()
    val goToInterestAlarmSettingFragmentEvent: SingleLiveData<Boolean> = _goToInterestAlarmSettingFragmentEvent

    private val _goToRegionAlarmSettingFragmentEvent = MutableSingleLiveData<Boolean>()
    val goToRegionAlarmSettingFragmentEvent: SingleLiveData<Boolean> = _goToRegionAlarmSettingFragmentEvent

    private val _backEvent = MutableSingleLiveData<Boolean>()
    val backEvent: SingleLiveData<Boolean> = _backEvent

    private val _openSourceEvent = MutableSingleLiveData<Boolean>()
    val openSourceEvent: SingleLiveData<Boolean> = _openSourceEvent

    fun openSourceClick() {
        _openSourceEvent.setValue(true)
    }

    fun backClick() {
        _backEvent.setValue(true)
    }

    fun goToInterestAlarmSettingFragment() {
        _goToInterestAlarmSettingFragmentEvent.setValue(true)
    }

    fun goToRegionAlarmSettingFragment() {
        _goToRegionAlarmSettingFragmentEvent.setValue(true)
    }

    fun logoutClick() {
        _logoutClickEvent.setValue(true)
    }

    fun withdrawalClick() {
        _withdrawalClickEvent.setValue(true)
    }

    suspend fun getGoogleAccessToken(clientId: String, secretId: String, authCode: String): String {
        var googleAccessToken = ""
        withContext(viewModelScope.coroutineContext) {
            val googleResponse = googleApiRepository.getGoogleAccessToken(clientId, secretId, authCode)
            googleResponse.onSuccess { googleAccessToken = data.accessToken ?: "" }
        }
        return googleAccessToken
    }

    suspend fun withdrawal(googleAccessToken: String? = null) {
        val withdrawalResponse = settingRepository.withdrawal(GoogleToken(googleAccessToken))
        withContext(Main) {
            val statusCode = if(withdrawalResponse is ApiResponse.Success) withdrawalResponse.statusCode.code
            else null
            _withdrawalSuccessfulEvent.setValue(statusCode)
        }
    }
}