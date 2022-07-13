package com.finpo.app.ui.community_detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.CommentContent
import com.finpo.app.model.remote.WritingContent
import com.finpo.app.repository.CommunityRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.Paging
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
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

    private val _commentList = MutableLiveData<List<CommentContent?>>()
    val commentList: LiveData<List<CommentContent?>> = _commentList

    private val _keyBoardHideEvent = MutableSingleLiveData<Boolean>()
    val keyBoardHideEvent: SingleLiveData<Boolean> = _keyBoardHideEvent

    private val _moreClickEvent = MutableSingleLiveData<Boolean>()
    val moreClickEvent: SingleLiveData<Boolean> = _moreClickEvent

    private val _editPostClickEvent = MutableSingleLiveData<Boolean>()
    val editPostClickEvent: SingleLiveData<Boolean> = _editPostClickEvent

    private val _deletePostClickEvent = MutableSingleLiveData<Boolean>()
    val deletePostClickEvent: SingleLiveData<Boolean> = _deletePostClickEvent

    private val _goToCommunityHomeFragmentEvent = MutableSingleLiveData<Boolean>()
    val goToCommunityHomeFragmentEvent: SingleLiveData<Boolean> = _goToCommunityHomeFragmentEvent

    private val _deleteItemClickEvent = MutableSingleLiveData<CommentContent>()
    val deleteItemClickEvent: SingleLiveData<CommentContent> = _deleteItemClickEvent

    private val _refreshed = MutableLiveData<Boolean>()
    val refreshed: LiveData<Boolean> = _refreshed

    val comment = MutableLiveData<String>()

    fun refreshData() {
        _refreshed.value = true
        getInitData()
        _refreshed.value = false
    }

    fun showDeleteCommentDialog(data: CommentContent) {
        _deleteItemClickEvent.setValue(data)
    }

    private fun changeCommentCount(diff: Int) {
        _writingContent.value?.countOfComment = _writingContent.value?.countOfComment?.plus(diff) ?: 0
        _writingContent.value = _writingContent.value
    }

    fun deleteComment(data: CommentContent) {
        viewModelScope.launch {
            val deleteResponse = communityRepository.deleteComment(data.id)
            deleteResponse.onSuccess {
                changeCommentCount(-1)
                changeComment()
            }
        }
    }

    fun deletePostClick() {
        _deletePostClickEvent.setValue(true)
    }

    fun deletePost() {
        viewModelScope.launch {
            val deleteResponse = communityRepository.deleteWriting(detailId)
            deleteResponse.onSuccess { _goToCommunityHomeFragmentEvent.setValue(true) }
        }
    }

    fun editPostClick() {
        _editPostClickEvent.setValue(true)
    }

    fun moreClick() {
        _moreClickEvent.setValue(true)
    }

    fun postComment() {
        viewModelScope.launch {
            val postResponse = communityRepository.postComment(detailId, comment.value ?: "")
            postResponse.onSuccess {
                if(paging.isLastPage) {
                    val tempCommentList = _commentList.value?.toMutableList() ?: mutableListOf()
                    tempCommentList.add(data.data)
                    _commentList.value = tempCommentList
                }
                comment.value = ""
                _keyBoardHideEvent.setValue(true)
                changeCommentCount(1)
            }
        }
    }

    //TODO 코루틴 학습 후 리팩토링
    fun getInitData() {
        viewModelScope.launch {
            val detailResponse = communityRepository.getWritingDetail(detailId)
            if(detailResponse !is ApiResponse.Success) return@launch
            _writingContent.value = detailResponse.data.data

            paging.resetPage()
            val commentResponse = communityRepository.getComment(detailId, paging.page.value ?: 0)
            if(commentResponse !is ApiResponse.Success) return@launch
            paging.loadData(
                commentResponse.data.data.content.toMutableList(),
                commentResponse.data.data.last, _commentList,
                paging.changeData()
            )
        }
    }

    private fun changeComment() {
        paging.resetPage()

        viewModelScope.launch {
            val commentResponse = communityRepository.getComment(detailId, paging.page.value ?: 0)
            commentResponse.onSuccess {
                paging.loadData(
                    data.data.content.toMutableList(),
                    data.data.last, _commentList,
                    paging.changeData()
                )
            }
        }
    }

    fun addComment() {
        if(paging.isLastPage || paging.page.value == 0) return
        viewModelScope.launch {
            val historyResponse = communityRepository.getComment(detailId, paging.page.value ?: 0)
            historyResponse.onSuccess {
                paging.loadData(
                    data.data.content.toMutableList(),
                    data.data.last, _commentList,
                    paging.addData()
                )
            }
        }
    }
}