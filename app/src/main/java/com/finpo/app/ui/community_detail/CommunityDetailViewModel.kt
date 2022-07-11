package com.finpo.app.ui.community_detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.CommentContent
import com.finpo.app.model.remote.WritingContent
import com.finpo.app.repository.CommunityRepository
import com.finpo.app.utils.Paging
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
    val paging: Paging<CommentContent>
) : ViewModel() {
    var detailId: Int = 0
    private val _writingContent = MutableLiveData<WritingContent>()
    val writingContent: LiveData<WritingContent> = _writingContent

    fun getWritingDetail() {
        viewModelScope.launch {
            val detailResponse = communityRepository.getWritingDetail(detailId)
            detailResponse.onSuccess {
                _writingContent.value = data.data
            }
        }
    }

    fun getComment() {
        viewModelScope.launch {
            val commentResponse = communityRepository.getComment(detailId, paging.page.value ?: 0)
            commentResponse.onSuccess {
                Log.d("CommunityDetailViewModel", "${data.data}")
            }
        }
    }
}