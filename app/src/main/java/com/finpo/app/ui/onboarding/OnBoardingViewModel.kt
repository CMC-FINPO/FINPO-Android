package com.finpo.app.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.PAGE
import com.finpo.app.utils.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor() : ViewModel() {
    private val _currentPage = MutableLiveData(0)
    val currentPage: LiveData<Int> = _currentPage

    private val _goToIntroActivityEvent = MutableSingleLiveData<Boolean>()
    val goToIntroActivityEvent: SingleLiveData<Boolean> = _goToIntroActivityEvent

    val isLastPage = MutableLiveData(false)

    fun goToIntroActivity() {
        _goToIntroActivityEvent.setValue(true)
    }

    fun goToLastPage() {
        _currentPage.value = 3
    }

    fun prevPage() {
        _currentPage.value = _currentPage.value?.minus(1)
    }
}