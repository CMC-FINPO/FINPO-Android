package com.finpo.app.ui.community_comment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.repository.CommunityRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityCommentViewModel @Inject constructor(
    private val communityRepository: CommunityRepository
) : ViewModel() {
    private val _backClick = MutableSingleLiveData<Boolean>()
    val backClick: SingleLiveData<Boolean> = _backClick

    private val _goToCommunityDetailFragmentEvent = MutableSingleLiveData<Boolean>()
    val goToCommunityDetailFragmentEvent: SingleLiveData<Boolean> = _goToCommunityDetailFragmentEvent

    val editTextInput = MutableLiveData<String>()

    var commentId: Int = 0

    fun backClick() {
        _backClick.setValue(true)
    }

    fun finishClick() {
        viewModelScope.launch {
            val response = communityRepository.editComment(commentId, editTextInput.value ?: "")
            response.onSuccess { _goToCommunityDetailFragmentEvent.setValue(true) }
        }
    }
}