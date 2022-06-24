package com.finpo.app.ui.intro.login

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.di.FinpoApplication
import com.finpo.app.model.remote.TokenResponse
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
    private val googleApiRepository: GoogleLoginRepository
) : ViewModel() {
    private val _kakaoLoginEvent = MutableSingleLiveData<Boolean>()
    val kakaoLoginEvent: SingleLiveData<Boolean> = _kakaoLoginEvent

    private val _googleLoginEvent = MutableSingleLiveData<Boolean>()
    val googleLoginEvent: SingleLiveData<Boolean> = _googleLoginEvent

    private val _isLoginSuccessfulEvent = MutableSingleLiveData<TokenResponse>()
    val isLoginSuccessfulEvent: SingleLiveData<TokenResponse> = _isLoginSuccessfulEvent

    private val _needRegisterEvent = MutableSingleLiveData<TokenResponse>()
    val needRegisterEvent: SingleLiveData<TokenResponse> = _needRegisterEvent

    var acToken = ""
    var oAuthType = ""
    var profileImage: Bitmap? = null

    fun getGoogleAccessToken(clientId: String, secretId: String, authCode: String) {
        viewModelScope.launch {
            val googleResponse = googleApiRepository.getGoogleAccessToken(clientId, secretId, authCode)
            googleResponse.onSuccess {
                acToken = data.accessToken
                loginFinpoByGoogle(data.accessToken)
            }
        }
    }

    private fun loginFinpoByGoogle(acToken: String) {
        viewModelScope.launch {
            val loginByGoogleResponse = introRepository.loginByGoogle(acToken)
            processLoginResponse(loginByGoogleResponse)
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
            processLoginResponse(loginByKakaoResponse)
        }
    }

    private fun processLoginResponse(loginResponse: ApiResponse<TokenResponse>) {
        loginResponse.onSuccess {
            when (statusCode.code) {
                200 -> _isLoginSuccessfulEvent.setValue(data)
                202 -> _needRegisterEvent.setValue(data)
            }
        }
    }
}