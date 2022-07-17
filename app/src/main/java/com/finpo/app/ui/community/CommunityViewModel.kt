package com.finpo.app.ui.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.WritingContent
import com.finpo.app.model.remote.WritingResponse
import com.finpo.app.repository.CommunityRepository
import com.finpo.app.ui.common.BaseViewModel
import com.finpo.app.ui.common.CommunityLikeBookmarkViewModel
import com.finpo.app.ui.community.search.CommunitySearchLiveData
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
    val searchLiveData: CommunitySearchLiveData,
    val likeBookmarkViewModel: CommunityLikeBookmarkViewModel,
    val paging: Paging<WritingContent>
) : BaseViewModel() {
    private val _writingList = MutableLiveData<List<WritingContent?>>()
    val writingList: LiveData<List<WritingContent?>> = _writingList

    private val _writingSize = MutableLiveData<Int>()
    val writingSize: LiveData<Int> = _writingSize

    private val _spinnerPosition = MutableLiveData<Int>()
    val spinnerPosition: LiveData<Int> = _spinnerPosition

    private val _goToPostFragmentEvent = MutableSingleLiveData<Boolean>()
    val goToPostFragmentEvent: SingleLiveData<Boolean> = _goToPostFragmentEvent

    private val _bottomSheetShowEvent = MutableSingleLiveData<Boolean>()
    val bottomSheetShowEvent: SingleLiveData<Boolean> = _bottomSheetShowEvent

    private val _bottomSheetDismissEvent = MutableSingleLiveData<Boolean>()
    val bottomSheetDismissEvent: SingleLiveData<Boolean> = _bottomSheetDismissEvent

    private val _goToDetailFragmentEvent = MutableSingleLiveData<Int>()
    val goToDetailFragmentEvent: SingleLiveData<Int> = _goToDetailFragmentEvent

    private val _refreshed = MutableLiveData<Boolean>()
    val refreshed: LiveData<Boolean> = _refreshed

    private val _searchClickEvent = MutableSingleLiveData<Boolean>()
    val searchClickEvent: SingleLiveData<Boolean> = _searchClickEvent

    private val _updateRecyclerViewItemEvent = MutableSingleLiveData<Pair<Int, WritingContent>>()
    val updateRecyclerViewItemEvent: SingleLiveData<Pair<Int, WritingContent>> = _updateRecyclerViewItemEvent

    fun searchClick() {
        _searchClickEvent.setValue(true)
    }

    fun goToDetailFragment(id: Int) {
        _goToDetailFragmentEvent.setValue(id)
    }

    fun showBottomSheetDialog() {
        _bottomSheetShowEvent.setValue(true)
    }

    fun spinnerItemClick(position: Int) {
        _spinnerPosition.value = position
        _bottomSheetDismissEvent.setValue(true)
        changeWriting()
    }

    fun initData() {
        _writingSize.value = 0
        changeWriting()
    }

    fun refreshWriting() {
        _refreshed.value = true
        changeWriting()
        _refreshed.value = false
    }

    fun clearWriting() {
        _writingSize.value = 0
        _writingList.value = listOf()
    }

    fun changeWriting() {
        paging.resetPage()
        searchLiveData.showInitView.value = false

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

    fun addWriting() {
        if (paging.isLastPage || paging.page.value == 0) return

        viewModelScope.launch {
            val writingResponse = getWritingResponse()
            writingResponse.onSuccess {
                paging.loadData(
                    data.data.content.toMutableList(),
                    data.data.last, _writingList,
                    paging.addData()
                )
            }
        }
    }

    private suspend fun getWritingResponse(): ApiResponse<WritingResponse> {
        return communityRepository.getWriting(
            content = searchLiveData.searchText,
            page = paging.page.value ?: 0,
            sort = listOf(SORT_COMMUNITY[spinnerPosition.value ?: 0])
        )
    }

    fun goToPostFragment() {
        _goToPostFragmentEvent.setValue(true)
    }

    fun checkContentChanged(data: WritingContent) {
        val position = _writingList.value?.indexOfFirst { data.id == it!!.id } ?: return
        if(position == -1) return
        val tempData = _writingList.value!!.toMutableList()
        tempData[position] = data
        _writingList.value = tempData
        _updateRecyclerViewItemEvent.setValue(Pair(position, data))
    }
}