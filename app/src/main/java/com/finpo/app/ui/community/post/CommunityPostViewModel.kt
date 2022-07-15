package com.finpo.app.ui.community.post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.PostWritingRequest
import com.finpo.app.repository.CommunityRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityPostViewModel @Inject constructor(
    private val communityRepository: CommunityRepository
) : ViewModel() {
    var id = -1

    val editTextInput = MutableLiveData<String>()

    private val _backEvent = MutableSingleLiveData<Boolean>()
    val backEvent: SingleLiveData<Boolean> = _backEvent

    private val _showPreparationToastEvent = MutableSingleLiveData<Boolean>()
    val showPreparationToastEvent: SingleLiveData<Boolean> = _showPreparationToastEvent

    private val _goToCommunityHomeFragmentEvent = MutableSingleLiveData<Boolean>()
    val goToCommunityHomeFragmentEvent: SingleLiveData<Boolean> = _goToCommunityHomeFragmentEvent

    fun showPreparationToast() {
        _showPreparationToastEvent.setValue(true)
    }

    fun backClick() {
        _backEvent.setValue(true)
    }

    fun finishClick() {
        if(id == -1) postWriting()
        else putWriting()
    }

    fun postWriting() {
        if(editTextInput.value.isNullOrEmpty()) return

        viewModelScope.launch {
            val postResponse = communityRepository.postWriting(PostWritingRequest(content = editTextInput.value ?: ""))
            postResponse.onSuccess {
                _goToCommunityHomeFragmentEvent.setValue(true)
            }
        }
    }

    private fun putWriting() {
        if(editTextInput.value.isNullOrEmpty()) return

        viewModelScope.launch {
            val postResponse = communityRepository.putWriting(id, PostWritingRequest(content = editTextInput.value ?: ""))
            postResponse.onSuccess {
                _goToCommunityHomeFragmentEvent.setValue(true)
            }
        }
    }
}