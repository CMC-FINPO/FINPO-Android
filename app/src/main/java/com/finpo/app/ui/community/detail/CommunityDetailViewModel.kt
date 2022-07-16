package com.finpo.app.ui.community.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.CommentContent
import com.finpo.app.model.remote.IdReason
import com.finpo.app.model.remote.WritingContent
import com.finpo.app.repository.BlockRepository
import com.finpo.app.repository.CommunityRepository
import com.finpo.app.repository.ReportRepository
import com.finpo.app.ui.common.BaseViewModel
import com.finpo.app.ui.common.CommunityLikeBookmarkViewModel
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.POST
import com.finpo.app.utils.Paging
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val reportRepository: ReportRepository,
    private val blockRepository: BlockRepository,
    val likeBookmarkViewModel: CommunityLikeBookmarkViewModel,
    val paging: Paging<CommentContent>
) : BaseViewModel() {
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

    private val _deleteSuccessfulEvent = MutableSingleLiveData<Boolean>()
    val deleteSuccessfulEvent: SingleLiveData<Boolean> = _deleteSuccessfulEvent

    private val _deleteItemClickEvent = MutableSingleLiveData<CommentContent>()
    val deleteItemClickEvent: SingleLiveData<CommentContent> = _deleteItemClickEvent

    private val _refreshed = MutableLiveData<Boolean>()
    val refreshed: LiveData<Boolean> = _refreshed

    private val _goToCommunityCommentFragmentEvent = MutableSingleLiveData<CommentContent>()
    val goToCommunityCommentFragmentEvent: SingleLiveData<CommentContent> = _goToCommunityCommentFragmentEvent

    private val _reportReasonList = MutableLiveData<List<IdReason>>()
    val reportReason: LiveData<List<IdReason>> = _reportReasonList

    private val _showReportDialog = MutableSingleLiveData<Boolean>()
    val showReportDialog: SingleLiveData<Boolean> = _showReportDialog

    private val _showBlockDialog = MutableSingleLiveData<Boolean>()
    val showBlockDialog: SingleLiveData<Boolean> = _showBlockDialog

    private val _dismissBottomSheetEvent = MutableSingleLiveData<Boolean>()
    val dismissBottomSheetEvent: SingleLiveData<Boolean> = _dismissBottomSheetEvent

    private val _showReportFinishAlertDialog = MutableSingleLiveData<Boolean>()
    val showReportFinishAlertDialog: SingleLiveData<Boolean> = _showReportFinishAlertDialog

    private val _showBlockFinishAlertDialog = MutableSingleLiveData<Boolean>()
    val showBlockFinishAlertDialog: SingleLiveData<Boolean> = _showBlockFinishAlertDialog

    val comment = MutableLiveData<String>()

    private var reportBlockType = 0
    private var reportBlockId = 0

    fun report(reportContentId: Int) {
        viewModelScope.launch {
            val response = when (reportBlockType) {
                POST -> reportRepository.reportPost(reportBlockId, reportContentId)
                else -> reportRepository.reportComment(reportBlockId, reportContentId)
            }
            response.onSuccess {
                _dismissBottomSheetEvent.setValue(true)
                _showReportFinishAlertDialog.setValue(true)
            }
        }
    }

    fun showReportDialog(reportType: Int, reportId: Int) {
        this.reportBlockType = reportType
        this.reportBlockId = reportId
        _showReportDialog.setValue(true)
    }

    fun block() {
        viewModelScope.launch {
            val response = when (reportBlockType) {
                POST -> blockRepository.blockPost(reportBlockId)
                else -> blockRepository.blockComment(reportBlockId)
            }
            response.onSuccess {
                _showBlockFinishAlertDialog.setValue(true)
            }
        }
    }

    fun showBlockDialog(blockType: Int, blockId: Int) {
        reportBlockType = blockType
        reportBlockId = blockId
        _showBlockDialog.setValue(true)
    }

    fun dismissReportDialog() {
        _dismissBottomSheetEvent.setValue(true)
    }

    fun getReportReason() {
        viewModelScope.launch {
            val response = reportRepository.getReportReason()
            response.onSuccess { _reportReasonList.value = data.data }
        }
    }

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

    fun editComment(data: CommentContent) {
        _goToCommunityCommentFragmentEvent.setValue(data)
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
            deleteResponse.onSuccess { _deleteSuccessfulEvent.setValue(true) }
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
            if(detailResponse.data.data.isUserBlocked == true) _deleteSuccessfulEvent.setValue(true)

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