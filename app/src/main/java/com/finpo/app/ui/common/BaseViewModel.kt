package com.finpo.app.ui.common

import androidx.lifecycle.ViewModel
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData

open class BaseViewModel : ViewModel() {
    private val _backClickEvent = MutableSingleLiveData<Boolean>()
    val backClickEvent: SingleLiveData<Boolean> = _backClickEvent

    fun backClick() {
        _backClickEvent.setValue(true)
    }
}