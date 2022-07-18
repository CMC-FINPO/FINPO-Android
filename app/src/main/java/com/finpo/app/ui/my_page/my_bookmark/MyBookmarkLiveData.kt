package com.finpo.app.ui.my_page.my_bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.WritingContent
import com.finpo.app.repository.MyInfoRepository
import com.finpo.app.ui.common.CommunityLikeBookmarkViewModel
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyBookmarkLiveData @Inject constructor(
    private val myInfoRepository: MyInfoRepository,
    val likeBookmarkViewModel: CommunityLikeBookmarkViewModel,
) : ViewModel() {
    private val _writingList = MutableLiveData<List<WritingContent?>>()
    val writingList: LiveData<List<WritingContent?>> = _writingList

    private val _updateRecyclerViewItemEvent = MutableSingleLiveData<Pair<Int, WritingContent>>()
    val updateRecyclerViewItemEvent: SingleLiveData<Pair<Int, WritingContent>> = _updateRecyclerViewItemEvent

    private val _writingSize = MutableLiveData(0)
    val writingSize: LiveData<Int> = _writingSize

    private val _refreshed = MutableLiveData<Boolean>()
    val refreshed: LiveData<Boolean> = _refreshed

    fun refreshBookmark() {
        _refreshed.value = true
        changeMyBookmark()
        _refreshed.value = false
    }

    fun removeMyBookmark(content: WritingContent) {
        val data = _writingList.value?.toMutableList()
        val index = data?.indexOfFirst { it?.id == content.id }
        index?.let { data.removeAt(index) }
        _writingList.value = data ?: mutableListOf()
        _writingSize.value = _writingSize.value?.minus(1)
    }

    fun changeMyBookmark() {
        viewModelScope.launch {
            val response = myInfoRepository.getMyWritingBookmark()
            response.onSuccess {
                _writingSize.value = data.data.totalElements
                _writingList.value = data.data.content
            }
        }
    }

    fun checkContentChanged(data: WritingContent) {
        val position = _writingList.value?.indexOfFirst { data.id == it!!.id } ?: return
        if(position == -1) return
        _writingList.value!![position]?.apply {
            isLiked = data.isLiked
            likes = data.likes
            isBookmarked = data.isBookmarked
            countOfComment = data.countOfComment
            hits = data.hits
        }
        _updateRecyclerViewItemEvent.setValue(Pair(position, data))
    }
}