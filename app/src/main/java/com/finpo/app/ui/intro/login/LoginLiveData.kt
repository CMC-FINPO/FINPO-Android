package com.finpo.app.ui.intro.login

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.di.FinpoApplication
import com.finpo.app.repository.GoogleLoginRepository
import com.finpo.app.repository.IntroRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginLiveData @Inject constructor(
    val introRepository: IntroRepository,
    val googleApiRepository: GoogleLoginRepository
) : ViewModel() {
    private val _kakaoLoginEvent = MutableSingleLiveData<Boolean>()
    val kakaoLoginEvent: SingleLiveData<Boolean> = _kakaoLoginEvent

    private val _googleLoginEvent = MutableSingleLiveData<Boolean>()
    val googleLoginEvent: SingleLiveData<Boolean> = _googleLoginEvent

    private val _isLoginSuccessfulEvent = MutableSingleLiveData<Boolean>()
    val isLoginSuccessfulEvent: SingleLiveData<Boolean> = _isLoginSuccessfulEvent

    var acToken = ""
    var profileImage: Bitmap? = null

    fun getGoogleAccessToken(clientId: String, secretId: String, authCode: String) {
        viewModelScope.launch {
            val googleResponse = googleApiRepository.getGoogleAccessToken(clientId, secretId, authCode)
            googleResponse.onSuccess {
                Log.d("GoogleLogin","${data.accessToken}")
            }
        }
    }

    fun loginGoogle() {
        _googleLoginEvent.setValue(true)
    }

    fun loginKakao() {
        _kakaoLoginEvent.setValue(true)
    }

    fun loginFinpoByKakao(acToken: String) {
        viewModelScope.launch {
            val loginByKakaoResponse = introRepository.loginByKakao(acToken)
            if(loginByKakaoResponse is ApiResponse.Success) {
                if(loginByKakaoResponse.data.data.accessToken != null) {
                    FinpoApplication.encryptedPrefs.saveAccessToken(loginByKakaoResponse.data.data.accessToken!!)
                    FinpoApplication.encryptedPrefs.saveRefreshToken(loginByKakaoResponse.data.data.refreshToken!!)
                }
                _isLoginSuccessfulEvent.setValue(loginByKakaoResponse.data.data.accessToken != null)
            }
        }
    }
}