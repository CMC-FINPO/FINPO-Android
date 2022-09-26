package com.finpo.app.ui.community.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.CommentContent
import com.finpo.app.model.remote.IdReason
import com.finpo.app.model.remote.ImageOrder
import com.finpo.app.model.remote.WritingContent
import com.finpo.app.repository.BlockRepository
import com.finpo.app.repository.CommunityRepository
import com.finpo.app.repository.ReportRepository
import com.finpo.app.ui.common.BaseViewModel
import com.finpo.app.ui.common.CommunityLikeBookmarkViewModel
import com.finpo.app.utils.*
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.suspendOnFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    private val _keyBoardShowEvent = MutableSingleLiveData<Boolean>()
    val keyBoardShowEvent: SingleLiveData<Boolean> = _keyBoardShowEvent

    private val _moreClickEvent = MutableSingleLiveData<Boolean>()
    val moreClickEvent: SingleLiveData<Boolean> = _moreClickEvent

    private val _editPostClickEvent = MutableSingleLiveData<Boolean>()
    val editPostClickEvent: SingleLiveData<Boolean> = _editPostClickEvent

    private val _deletePostClickEvent = MutableSingleLiveData<Boolean>()
    val deletePostClickEvent: SingleLiveData<Boolean> = _deletePostClickEvent

    private val _deleteSuccessfulEvent = MutableSingleLiveData<Boolean>()
    val deleteSuccessfulEvent: SingleLiveData<Boolean> = _deleteSuccessfulEvent

    private val _deleteItemClickEvent = MutableSingleLiveData<Int>()
    val deleteItemClickEvent: SingleLiveData<Int> = _deleteItemClickEvent

    private val _refreshed = MutableLiveData<Boolean>()
    val refreshed: LiveData<Boolean> = _refreshed

    private val _goToCommunityCommentFragmentEvent = MutableSingleLiveData<Pair<Int, String>>()
    val goToCommunityCommentFragmentEvent: SingleLiveData<Pair<Int, String>> = _goToCommunityCommentFragmentEvent

    private val _goToImageViewerFragmentEvent = MutableSingleLiveData<Pair<ImageOrder, Int>>()
    val goToImageViewerFragmentEvent: SingleLiveData<Pair<ImageOrder, Int>> = _goToImageViewerFragmentEvent

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

    private val _isReplyMode = MutableLiveData(false)
    val isReplyMode: LiveData<Boolean> = _isReplyMode

    private val _dismissPopupEvent = MutableSingleLiveData<Boolean>()
    val dismissPopupEvent: SingleLiveData<Boolean> = _dismissPopupEvent

    private val _replyName = MutableLiveData("")
    val replyName: LiveData<String> = _replyName

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    val comment = MutableLiveData<String>()

    private var reportBlockType = 0
    private var reportBlockId = 0
    private var commentParentId = 0

    fun imageClickEvent(imageOrder: ImageOrder, index: Int) {
        _goToImageViewerFragmentEvent.setValue(Pair(imageOrder, index))
    }

    fun updateWritingContent(data: WritingContent) {
        _writingContent.value = data
    }

    fun setReplyMode(data: CommentContent) {
        _keyBoardShowEvent.setValue(true)
        _dismissPopupEvent.setValue(true)
        commentParentId = data.id
        _isReplyMode.value = true
        _replyName.value = data.user?.nickname ?: "(알 수 없음)"
    }

    fun cancelReply() {
        _isReplyMode.value = false
    }

    fun postComment() {
        viewModelScope.launch {
            if(isReplyMode.value == true) callCommentReplyApi()
            else callCommentApi()
        }
    }

    private suspend fun callCommentReplyApi() {
        val response =
            communityRepository.postCommentReply(detailId, commentParentId, comment.value ?: "")
        response.onSuccess {
            val position = _commentList.value?.indexOfFirst { it?.id == commentParentId } ?: -1
            if (position == -1) return@onSuccess

            val tempCommentList = _commentList.value?.deepCopy()
            if(tempCommentList?.get(position)?.childs == null) tempCommentList?.get(position)?.childs = mutableListOf()

            tempCommentList?.get(position)?.childs?.add(data.data)
            _commentList.value = tempCommentList!!

            _isReplyMode.value = false
            comment.value = ""
            _keyBoardHideEvent.setValue(true)
            changeCommentCount(1)
        }
    }

    private suspend fun callCommentApi() {
        val postResponse = communityRepository.postComment(detailId, comment.value ?: "")
        postResponse.onSuccess {
            if (paging.isLastPage) {
                val tempCommentList = _commentList.value?.toMutableList() ?: mutableListOf()
                tempCommentList.add(data.data)
                _commentList.value = tempCommentList
            }
            comment.value = ""
            _keyBoardHideEvent.setValue(true)
            changeCommentCount(1)
        }
    }

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

    fun showDeleteCommentDialog(id: Int) {
        _deleteItemClickEvent.setValue(id)
    }

    private fun changeCommentCount(diff: Int) {
        _writingContent.value?.countOfComment = _writingContent.value?.countOfComment?.plus(diff) ?: 0
        _writingContent.value = _writingContent.value
    }

    fun editComment(commentId: Int, comment: String) {
        _goToCommunityCommentFragmentEvent.setValue(Pair(commentId, comment))
    }

    fun deleteComment(data: Int) {
        viewModelScope.launch {
            val deleteResponse = communityRepository.deleteComment(data)
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

    fun getInitData() {
        viewModelScope.launch {
            val detailResponse = communityRepository.getWritingDetail(detailId)
            detailResponse.onSuccess {
                _writingContent.value = data.data
                Log.d("test","${data.data.imgs}")
            }

            changeComment()
            _isLoading.value = false
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