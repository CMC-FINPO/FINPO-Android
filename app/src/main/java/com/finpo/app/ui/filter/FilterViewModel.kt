package com.finpo.app.ui.filter

import android.util.Log
import androidx.lifecycle.*
import com.finpo.app.model.local.IdName
import com.finpo.app.model.remote.CategoryChild
import com.finpo.app.model.remote.CategoryChildFormat
import com.finpo.app.model.remote.MyRegionResponse
import com.finpo.app.model.remote.RegionRequest
import com.finpo.app.repository.FilterRepository
import com.finpo.app.utils.*
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val filterRepository: FilterRepository
): ViewModel() {
    private val _filterRegionSelTextList = MutableLiveData<MutableList<IdName>>()
    val filterRegionSelTextList: LiveData<MutableList<IdName>> = _filterRegionSelTextList

    private val _filterRegionSelCount = MutableLiveData<Int>()
    val filterRegionSelCount: LiveData<Int> = _filterRegionSelCount

    private val _filterCategoryData = MutableLiveData<List<CategoryChildFormat>>()
    val filterCategoryData: LiveData<List<CategoryChildFormat>> = _filterCategoryData

    private val _userCategoryData = MutableLiveData<IntArray>()
    val userCategoryData: LiveData<IntArray> = _userCategoryData

    private val _clearEvent = MutableSingleLiveData<Boolean>()
    val clearEvent: SingleLiveData<Boolean> = _clearEvent

    private val _backEvent = MutableSingleLiveData<Boolean>()
    val backEvent: SingleLiveData<Boolean> = _backEvent

    private val _goToHomeFragmentEvent = MutableSingleLiveData<Boolean>()
    val goToHomeFragmentEvent: SingleLiveData<Boolean> = _goToHomeFragmentEvent

    val isFilterButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSourceList(_filterRegionSelCount, _userCategoryData) {
            isStatusPurposeValid()
        }
    }

    private fun isStatusPurposeValid(): Boolean = (_filterRegionSelCount.value != 0 && _userCategoryData.value?.isEmpty() == false)

    fun backClick() {
        _backEvent.setValue(true)
    }

    fun goToHomeFragment() {
        _goToHomeFragmentEvent.setValue(true)
    }

    fun clearFilter() {
        _clearEvent.setValue(true)
    }

    fun categoryClick(id: Int) {
        val userCategoryData = _userCategoryData.value?.toMutableList() ?: mutableListOf()
        if(id in userCategoryData)  userCategoryData.remove(id)
        else    userCategoryData.add(id)
        _userCategoryData.value = userCategoryData.toIntArray()
    }

    fun setCategories(data: IntArray) {
        _userCategoryData.value = data
    }

    fun clearRegion() {
        val detailRegionTextList = MutableList(MAX_FILTER_REGION_COUNT){IdName(0, "")}
        _filterRegionSelTextList.value = detailRegionTextList
        _filterRegionSelCount.value = 0
    }

    fun setRegion(data: List<IdName>) {
        _filterRegionSelTextList.value = data.toMutableList()
        _filterRegionSelCount.value = data.size
    }

    fun getCategory() {
        viewModelScope.launch {
            val filterResponse = filterRepository.getCategoryChildFormat()
            filterResponse.onSuccess {
                _filterCategoryData.value = data.data
            }
        }
    }

    fun deleteFilterRegion(deleteIndex: Int) {
        val detailRegionTextList = _filterRegionSelTextList.value!!
        for(i in deleteIndex until MAX_FILTER_REGION_COUNT - 1) {
            detailRegionTextList[i] = detailRegionTextList[i + 1]
        }
        detailRegionTextList[MAX_FILTER_REGION_COUNT - 1] = IdName(0, "")
        _filterRegionSelTextList.value = detailRegionTextList
        _filterRegionSelCount.value = _filterRegionSelCount.value!! - 1
    }
}