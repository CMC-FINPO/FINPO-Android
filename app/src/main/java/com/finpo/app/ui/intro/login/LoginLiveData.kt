package com.finpo.app.ui.intro.login

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.di.FinpoApplication
import com.finpo.app.repository.IntroRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginLiveData @Inject constructor(
    val introRepository: IntroRepository
) : ViewModel() {
    private val _kakaoLoginEvent = MutableSingleLiveData<Boolean>()
    val kakaoLoginEvent: SingleLiveData<Boolean> = _kakaoLoginEvent

    private val _isLoginSuccessfulEvent = MutableSingleLiveData<Boolean>()
    val isLoginSuccessfulEvent: SingleLiveData<Boolean> = _isLoginSuccessfulEvent

    var acToken = ""
    var profileImage: Bitmap? = null

    fun loginKakao() {
        _kakaoLoginEvent.setValue(true)
    }

    fun loginFinpoByKakao(acToken: String) {
        viewModelScope.launch {
            val data = introRepository.loginByKakao(acToken)
            if(data.isSuccessful && data.body() != null) {
                if(data.body()!!.data.accessToken != null) {
                    FinpoApplication.encryptedPrefs.saveAccessToken(data.body()!!.data.accessToken!!)
                    FinpoApplication.encryptedPrefs.saveRefreshToken(data.body()!!.data.refreshToken!!)
                }
                _isLoginSuccessfulEvent.setValue(data.body()!!.data.accessToken != null)
            }
        }
    }
}