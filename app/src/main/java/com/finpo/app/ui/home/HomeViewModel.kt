package com.finpo.app.ui.home

import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.MyRegion
import com.finpo.app.model.remote.PolicyContent
import com.finpo.app.model.remote.PolicyResponse
import com.finpo.app.repository.BookmarkRepository
import com.finpo.app.repository.MyInfoRepository
import com.finpo.app.repository.PolicyRepository
import com.finpo.app.utils.*
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val policyRepository: PolicyRepository,
    private val myInfoRepository: MyInfoRepository,
    private val bookmarkRepository: BookmarkRepository,
    val paging: Paging<PolicyContent>
) : ViewModel() {

    private val _policyList = MutableLiveData<List<PolicyContent?>>()
    val policyList: LiveData<List<PolicyContent?>> = _policyList

    private val _policySize = MutableLiveData<Int>()
    val policySize: LiveData<Int> = _policySize

    private val _spinnerPosition = MutableLiveData<Int>()
    val spinnerPosition: LiveData<Int> = _spinnerPosition

    private val _bottomSheetShowEvent = MutableSingleLiveData<Boolean>()
    val bottomSheetShowEvent: SingleLiveData<Boolean> = _bottomSheetShowEvent

    private val _bottomSheetDismissEvent = MutableSingleLiveData<Boolean>()
    val bottomSheetDismissEvent: SingleLiveData<Boolean> = _bottomSheetDismissEvent

    private val _keyBoardSearchEvent = MutableSingleLiveData<Boolean>()
    val keyBoardSearchEvent: SingleLiveData<Boolean> = _keyBoardSearchEvent

    private val _goToFilterFragmentEvent = MutableSingleLiveData<Boolean>()
    val goToFilterFragmentEvent: SingleLiveData<Boolean> = _goToFilterFragmentEvent

    private val _goToDetailFragmentEvent = MutableSingleLiveData<Int>()
    val goToDetailFragmentEvent: SingleLiveData<Int> = _goToDetailFragmentEvent

    private val _updateRecyclerViewItemEvent = MutableSingleLiveData<Pair<Int, PolicyContent>>()
    val updateRecyclerViewItemEvent: SingleLiveData<Pair<Int, PolicyContent>> = _updateRecyclerViewItemEvent

    private val _showBookmarkCountMaxToastEvent = MutableSingleLiveData<Boolean>()
    val showBookmarkCountMaxToastEvent: SingleLiveData<Boolean> = _showBookmarkCountMaxToastEvent

    val searchInputText = MutableLiveData<String>()
    var searchText = ""

    lateinit var regionIds: List<Int>
    lateinit var regionTextList: List<String>
    lateinit var categoryIds: List<Int>

    fun goToDetailFragment(id: Int) {
        _goToDetailFragmentEvent.setValue(id)
    }

    fun goToFilterFragment() {
        _goToFilterFragmentEvent.setValue(true)
    }

    fun showBottomSheetDialog() {
        _bottomSheetShowEvent.setValue(true)
    }

    fun spinnerItemClick(position: Int) {
        _spinnerPosition.value = position
        _bottomSheetDismissEvent.setValue(true)
        changePolicy()
    }

    fun getInitData() {
        _policySize.value = 0
        viewModelScope.launch {
            val myRegionResponse = myInfoRepository.getMyRegion()
            val myCategoryResponse = myInfoRepository.getMyCategory()
            if(myRegionResponse !is ApiResponse.Success || myCategoryResponse !is ApiResponse.Success)   return@launch
            regionIds = List(myRegionResponse.data.data.size) { myRegionResponse.data.data[it].region.id ?: 0 }
            initRegions(myRegionResponse.data.data)
            categoryIds = List(myCategoryResponse.data.data.size) { myCategoryResponse.data.data[it].category.id }
            changePolicy()
        }
    }

    private fun initRegions(data: List<MyRegion>) {
        val tempRegions = mutableListOf<String>()
        for(element in data) {
            if(element.region.parent == null) tempRegions.add("${element.region.name} ??????")
            else tempRegions.add("${element.region.parent.name} ${element.region.name}")
        }
        for(idx in data.size until MAX_FILTER_REGION_COUNT)
            tempRegions.add("")
        regionTextList = tempRegions
    }

    fun addPolicy() {
        if (paging.isLastPage || paging.page.value == 0) return

        viewModelScope.launch {
            val policyResponse = getPolicyResponse()
            policyResponse.onSuccess {
                paging.loadData(
                    data.data.content.toMutableList(),
                    data.data.last, _policyList,
                    paging.addData()
                )
            }
        }
    }

    fun clearPolicy() {
        _policySize.value = 0
        _policyList.value = listOf()
    }

    fun changePolicy() {
        paging.resetPage()

        if(::regionIds.isInitialized && ::regionTextList.isInitialized && ::categoryIds.isInitialized)
        viewModelScope.launch {
            val policyResponse = getPolicyResponse()
            policyResponse.onSuccess {
                paging.loadData(
                    data.data.content.toMutableList(),
                    data.data.last, _policyList,
                    paging.changeData()
                )
                _policySize.value = data.data.totalElements
            }
        }
    }

    private suspend fun getPolicyResponse(): ApiResponse<PolicyResponse> {
        return policyRepository.getPolicy(
            title = searchText,
            region = regionIds,
            category = categoryIds,
            page = paging.page.value ?: 0,
            sort = listOf(SORT_POLICY[spinnerPosition.value ?: 0])
        )
    }

    fun onEditTextSearchClick(actionId: Int): Boolean {
        return if(actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchText = searchInputText.value ?: ""
            changePolicy()
            _keyBoardSearchEvent.setValue(true)
            true
        } else false
    }

    fun bookmarkClick(data: PolicyContent) {
        viewModelScope.launch {
            val position = _policyList.value?.indexOfFirst { it?.id == data.id } ?: -1
            if(position == -1) return@launch

            if(!data.isInterest) {
                val addInterestPolicyResponse = bookmarkRepository.addInterestPolicy(data.id)
                addInterestPolicyResponse.onSuccess {
                    data.isInterest = !data.isInterest
                    _updateRecyclerViewItemEvent.setValue(Pair(position, data))
                }.onError { if(statusCode.code == 400) _showBookmarkCountMaxToastEvent.setValue(true) }

            } else {
                val deleteInterestPolicyResponse = bookmarkRepository.deleteInterestPolicy(data.id)
                deleteInterestPolicyResponse.onSuccess {
                    data.isInterest = !data.isInterest
                    _updateRecyclerViewItemEvent.setValue(Pair(position, data))
                }
            }

            _policyList.value!![position]!!.isInterest = data.isInterest
        }
    }

    fun checkBookmarkChanged(id: Int, isBookmarked: Boolean) {
        val position = policyList.value?.indexOfFirst { it?.id == id } ?: return
        if(position == -1)   return
        val data = policyList.value?.get(position)
        data?.isInterest = isBookmarked
        if(data != null) _updateRecyclerViewItemEvent.setValue(Pair(position, data))
    }
}