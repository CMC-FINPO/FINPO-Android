package com.finpo.app.ui.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.WritingContent
import com.finpo.app.model.remote.WritingResponse
import com.finpo.app.repository.CommunityRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.Paging
import com.finpo.app.utils.SORT_COMMUNITY
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
    val paging: Paging<WritingContent>
) : ViewModel() {
    private val _writingList = MutableLiveData<List<WritingContent?>>()
    val writingList: LiveData<List<WritingContent?>> = _writingList

    private val _writingSize = MutableLiveData<Int>()
    val writingSize: LiveData<Int> = _writingSize

    private val _spinnerPosition = MutableLiveData<Int>()
    val spinnerPosition: LiveData<Int> = _spinnerPosition

    private val _goToPostFragmentEvent = MutableSingleLiveData<Boolean>()
    val goToPostFragmentEvent: SingleLiveData<Boolean> = _goToPostFragmentEvent

    init {
        initData()
    }

    fun initData() {
        _writingSize.value = 0
        changeWriting()
    }

    fun changeWriting() {
        paging.resetPage()

        viewModelScope.launch {
            val writingResponse = getWritingResponse()
            writingResponse.onSuccess {
                paging.loadData(
                    data.data.content.toMutableList(),
                    data.data.last, _writingList,
                    paging.changeData()
                )
                _writingSize.value = data.data.totalElements
                Log.d("CommunityViewModel", "${_writingList.value}")
            }
        }
    }

    private suspend fun getWritingResponse(): ApiResponse<WritingResponse> {
        return communityRepository.getWriting(
            content = "",
            page = paging.page.value ?: 0,
            sort = listOf(SORT_COMMUNITY[spinnerPosition.value ?: 0])
        )
    }

    fun goToPostFragment() {
        _goToPostFragmentEvent.setValue(true)
    }
}