package com.finpo.app.ui.intro

import androidx.lifecycle.ViewModel
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import javax.inject.Inject

class IntroViewModel @Inject constructor(): ViewModel() {
    private val _kakaoLoginEvent = MutableSingleLiveData<Boolean>()
    val kakaoLoginEvent : SingleLiveData<Boolean>
        get() = _kakaoLoginEvent

    fun loginKakao() {
        _kakaoLoginEvent.setValue(true)
    }
}