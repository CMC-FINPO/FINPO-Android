package com.finpo.app.ui.intro

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import javax.inject.Inject

class IntroViewModel @Inject constructor(): ViewModel() {
    private val _kakaoLoginEvent = MutableSingleLiveData<Boolean>()
    val kakaoLoginEvent : SingleLiveData<Boolean> = _kakaoLoginEvent

    private val _currentPage = MutableLiveData<Int>()
    val currentPage: LiveData<Int> = _currentPage

    init {
        _currentPage.value = 0
    }

    fun loginKakao() {
        _kakaoLoginEvent.setValue(true)
    }

    fun nextPage() {
        _currentPage.value = _currentPage.value?.plus(1)
    }

    fun prevPage() {
        _currentPage.value = _currentPage.value?.minus(1)
    }
}