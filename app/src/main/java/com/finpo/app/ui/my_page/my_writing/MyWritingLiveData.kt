package com.finpo.app.ui.my_page.my_writing

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.WritingContent
import com.finpo.app.repository.MyInfoRepository
import com.finpo.app.ui.common.CommunityLikeBookmarkViewModel
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.Paging
import com.finpo.app.utils.SingleLiveData
import com.finpo.app.utils.deepCopy
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyWritingLiveData @Inject constructor(
    private val myInfoRepository: MyInfoRepository,
    val likeBookmarkViewModel: CommunityLikeBookmarkViewModel,
    val paging: Paging<WritingContent>
) : ViewModel() {
    private val _writingList = MutableLiveData<List<WritingContent?>>()
    val writingList: LiveData<List<WritingContent?>> = _writingList

    private val _refreshed = MutableLiveData<Boolean>()
    val refreshed: LiveData<Boolean> = _refreshed

    fun refreshWriting() {
        _refreshed.value = true
        changeMyWriting()
        _refreshed.value = false
    }

    fun changeMyWriting() {
        paging.resetPage()

        viewModelScope.launch {
            val response = myInfoRepository.getMyWriting(paging.page.value ?: 0)
            response.onSuccess {
                paging.loadData(
                    data.data.content.toMutableList(),
                    data.data.last, _writingList,
                    paging.changeData()
                )
            }
        }
    }

    fun addWriting() {
        if (paging.isLastPage || paging.page.value == 0) return

        viewModelScope.launch {
            val writingResponse = myInfoRepository.getMyWriting(paging.page.value ?: 0)
            writingResponse.onSuccess {
                paging.loadData(
                    data.data.content.toMutableList(),
                    data.data.last, _writingList,
                    paging.addData()
                )
            }
        }
    }

    fun checkContentChanged(data: WritingContent) {
        val position = _writingList.value?.indexOfFirst { data.id == it?.id } ?: return
        if(position == -1) return
        val tempWritingList = _writingList.value!!.deepCopy()
        tempWritingList[position] = data
        _writingList.value = tempWritingList
    }
}