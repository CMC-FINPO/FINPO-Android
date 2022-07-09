package com.finpo.app.ui.community

import androidx.lifecycle.ViewModel
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor() : ViewModel() {
    private val _goToPostFragmentEvent = MutableSingleLiveData<Boolean>()
    val goToPostFragmentEvent: SingleLiveData<Boolean> = _goToPostFragmentEvent

    fun goToPostFragment() {
        _goToPostFragmentEvent.setValue(true)
    }
}