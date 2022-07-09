package com.finpo.app.ui.community_post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommunityPostViewModel @Inject constructor() : ViewModel() {
    val editTextInput = MutableLiveData<String>()
    private val _backEvent = MutableSingleLiveData<Boolean>()
    val backEvent: SingleLiveData<Boolean> = _backEvent

    fun backClick() {
        _backEvent.setValue(true)
    }
}