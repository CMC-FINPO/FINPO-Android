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

    val comment = MutableLiveData<String>()

    fun showDeleteCommentDialog(data: CommentContent) {
        _deleteItemClickEvent.setValue(data)
    }

    fun deleteComment(data: CommentContent) {
        viewModelScope.launch {
            val deleteResponse = communityRepository.deleteComment(data.id)
            deleteResponse.onSuccess {
                val commentList = _commentList.value?.toMutableList() ?: mutableListOf()
                commentList.remove(data)
                _commentList.value = commentList
                paging.deleteData(data)
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
            }
        }
    }

    fun getWritingDetail() {
        viewModelScope.launch {
            val detailResponse = communityRepository.getWritingDetail(detailId)
            detailResponse.onSuccess {
                _writingContent.value = data.data
            }
        }
    }

    fun changeComment() {
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