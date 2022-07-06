package com.finpo.app.ui.home

import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.local.IdName
import com.finpo.app.model.remote.MyRegion
import com.finpo.app.model.remote.PolicyContent
import com.finpo.app.model.remote.PolicyResponse
import com.finpo.app.repository.BookmarkRepository
import com.finpo.app.repository.MyInfoRepository
import com.finpo.app.repository.PolicyRepository
import com.finpo.app.utils.*
import com.skydoves.sandwich.ApiResponse
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

    val searchText = MutableLiveData<String>()

    lateinit var regionIds: List<Int>
    lateinit var regions: List<IdName>
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
        val tempRegions = mutableListOf<IdName>()
        for(element in data) {
            if(element.region.parent == null) tempRegions.add(IdName(element.region.id,"${element.region.name} 전체"))
            else tempRegions.add(IdName(element.region.id,"${element.region.parent.name} ${element.region.name}"))
        }
        for(idx in data.size until MAX_FILTER_REGION_COUNT)
            tempRegions.add(IdName(null, ""))
        regions = tempRegions
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

    fun changePolicy() {
        paging.resetPage()

        viewModelScope.launch {
            val policyResponse = getPolicyResponse()
            policyResponse.onSuccess {
                paging.loadData(
                    data.data.content.toMutableList(),
                    data.data.last, _policyList,
                    paging.changeData()
                )
            }
        }
    }

    private suspend fun getPolicyResponse(): ApiResponse<PolicyResponse> {
        return policyRepository.getPolicy(
            title = searchText.value ?: "",
            region = regionIds,
            category = categoryIds,
            page = paging.page.value ?: 0,
            sort = listOf(SORT[spinnerPosition.value ?: 0])
        )
    }

    fun onEditTextSearchClick(actionId: Int): Boolean {
        return if(actionId == EditorInfo.IME_ACTION_SEARCH) {
            changePolicy()
            _keyBoardSearchEvent.setValue(true)
            true
        } else false
    }

    fun bookmarkClick(data: PolicyContent, position: Int) {
        viewModelScope.launch {
            if(!data.isInterest) {
                val addInterestPolicyResponse = bookmarkRepository.addInterestPolicy(data.id)
                data.isInterest = !data.isInterest
                if(addInterestPolicyResponse is ApiResponse.Success)    _updateRecyclerViewItemEvent.setValue(Pair(position, data))
            } else {
                val deleteInterestPolicyResponse = bookmarkRepository.deleteInterestPolicy(data.id)
                data.isInterest = !data.isInterest
                if(deleteInterestPolicyResponse is ApiResponse.Success)    _updateRecyclerViewItemEvent.setValue(Pair(position, data))
            }
        }
    }

    fun checkBookmarkChanged(id: Int, isBookmarked: Boolean) {
        val position = policyList.value?.indexOfFirst { it?.id == id } ?: return
        if(position == -1)   return
        val data = policyList.value!![position]
        data!!.isInterest = isBookmarked
        _updateRecyclerViewItemEvent.setValue(Pair(position, data))
    }
}