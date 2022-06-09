package com.finpo.app.ui.intro

import android.util.Log
import android.widget.CompoundButton
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finpo.app.ui.intro.login.LoginLiveData
import com.finpo.app.ui.intro.terms_conditions.TermsConditionsLiveData
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    val termsConditionsLiveData: TermsConditionsLiveData,
    val loginLiveData: LoginLiveData
) : ViewModel() {

    private val _currentPage = MutableLiveData<Int>()
    val currentPage: LiveData<Int> = _currentPage

    init {
        _currentPage.value = 0
    }

    fun nextPage() {
        //TODO 마지막 페이지, 중간 회원가입 완료 페이지인 경우 예외 처리 필요
        _currentPage.value = _currentPage.value?.plus(1)
    }

    fun prevPage() {
        _currentPage.value = _currentPage.value?.minus(1)
    }
}