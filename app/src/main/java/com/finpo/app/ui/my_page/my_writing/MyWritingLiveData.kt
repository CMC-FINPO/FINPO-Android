package com.finpo.app.ui.my_page.my_writing

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.WritingContent
import com.finpo.app.repository.MyInfoRepository
import com.finpo.app.utils.Paging
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyWritingLiveData @Inject constructor(
    private val myInfoRepository: MyInfoRepository,
    val paging: Paging<WritingContent>
) : ViewModel() {
    private val _writingList = MutableLiveData<List<WritingContent?>>()
    val writingList: LiveData<List<WritingContent?>> = _writingList

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
}