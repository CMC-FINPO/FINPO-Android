package com.finpo.app.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.WritingContent
import com.finpo.app.repository.CommunityRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.launch
import javax.inject.Inject

class CommunityLikeBookmarkViewModel @Inject constructor(
    private val communityRepository: CommunityRepository
) : ViewModel() {
    val updateRecyclerView = MutableSingleLiveData<WritingContent>()

    private val _likeClickErrorToastEvent = MutableSingleLiveData<Boolean>()
    val likeClickErrorToastEvent: SingleLiveData<Boolean> = _likeClickErrorToastEvent

    private val _bookmarkMaxToastEvent = MutableSingleLiveData<Boolean>()
    val bookmarkMaxToastEvent: SingleLiveData<Boolean> = _bookmarkMaxToastEvent

    fun likeClick(data: WritingContent) {
        if(data.isMine == true) _likeClickErrorToastEvent.setValue(true)

        viewModelScope.launch {
            val response =
                if (data.isLiked == true) communityRepository.deleteWritingLike(data.id)
                else communityRepository.putWritingLike(data.id)
            response.onSuccess {
                data.isLiked = !(data.isLiked ?: true)
                data.likes += if(data.isLiked == true) 1 else -1
                updateRecyclerView.setValue(data)
            }
        }
    }

    fun bookmarkClick(data: WritingContent) {

        viewModelScope.launch {
            val response =
                if (data.isBookmarked == true) communityRepository.deleteWritingBookmark(data.id)
                else communityRepository.putWritingBookmark(data.id)
            response.onSuccess {
                data.isBookmarked = !(data.isBookmarked ?: true)
                updateRecyclerView.setValue(data)
            }.onError { if(statusCode.code == 400) _bookmarkMaxToastEvent.setValue(true) }
        }
    }

}