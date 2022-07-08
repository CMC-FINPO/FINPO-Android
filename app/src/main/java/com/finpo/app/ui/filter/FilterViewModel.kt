package com.finpo.app.ui.filter

import androidx.lifecycle.*
import com.finpo.app.model.remote.CategoryChildFormat
import com.finpo.app.repository.CategoryRepository
import com.finpo.app.ui.common.RegionViewModel
import com.finpo.app.ui.filter.bottom_sheet.BottomSheetRegionViewModel
import com.finpo.app.utils.*
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    val bottomSheetRegionViewModel: BottomSheetRegionViewModel,
    val filterRegionViewModel: RegionViewModel
) : ViewModel() {
    private val _filterCategoryData = MutableLiveData<List<CategoryChildFormat>>()
    val filterCategoryData: LiveData<List<CategoryChildFormat>> = _filterCategoryData

    private val _userCategoryData = MutableLiveData<IntArray>()
    val userCategoryData: LiveData<IntArray> = _userCategoryData

    private val _clearEvent = MutableSingleLiveData<Boolean>()
    val clearEvent: SingleLiveData<Boolean> = _clearEvent

    private val _backEvent = MutableSingleLiveData<Boolean>()
    val backEvent: SingleLiveData<Boolean> = _backEvent

    private val _showBottomSheetEvent = MutableSingleLiveData<Boolean>()
    val showBottomSheetEvent: SingleLiveData<Boolean> = _showBottomSheetEvent

    private val _dismissBottomSheetEvent = MutableSingleLiveData<Boolean>()
    val dismissBottomSheetEvent: SingleLiveData<Boolean> = _dismissBottomSheetEvent

    private val _goToHomeFragmentEvent = MutableSingleLiveData<Boolean>()
    val goToHomeFragmentEvent: SingleLiveData<Boolean> = _goToHomeFragmentEvent

    val isFilterButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSourceList(filterRegionViewModel._regionCount, _userCategoryData) {
            isStatusPurposeValid()
        }
    }

    private fun isStatusPurposeValid(): Boolean = (filterRegionViewModel._regionCount.value != 0 && _userCategoryData.value?.isEmpty() == false)

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
        if (id in userCategoryData) userCategoryData.remove(id)
        else userCategoryData.add(id)
        _userCategoryData.value = userCategoryData.toIntArray()
    }

    fun setCategories(data: IntArray) {
        _userCategoryData.value = data
    }

    fun clearRegion() {
        val detailRegionTextList = MutableList(MAX_FILTER_REGION_COUNT) { "" }
        filterRegionViewModel.setRegionTextList(detailRegionTextList)
        filterRegionViewModel.regionIds = mutableListOf()
        filterRegionViewModel.setRegionCount(0)
    }

    fun setRegion(data: List<String>, ids: List<Int>) {
        filterRegionViewModel.MAX_COUNT = MAX_FILTER_REGION_COUNT
        filterRegionViewModel.setRegionTextList(data.toMutableList())
        filterRegionViewModel.setRegionCount(data.count { it != "" })
        filterRegionViewModel.regionIds = ids.toMutableList()
    }

    fun getCategory() {
        viewModelScope.launch {
            val filterResponse = categoryRepository.getCategoryChildFormat()
            filterResponse.onSuccess {
                _filterCategoryData.value = data.data
            }
        }
    }

    fun editFinishClick() {
        filterRegionViewModel.setRegionTextList(bottomSheetRegionViewModel.bottomFilterRegionViewModel.regionTextList.value ?: mutableListOf())
        filterRegionViewModel.regionIds = bottomSheetRegionViewModel.bottomFilterRegionViewModel.regionIds
        filterRegionViewModel.setRegionCount(bottomSheetRegionViewModel.bottomFilterRegionViewModel.regionCount.value ?: 0)
        _dismissBottomSheetEvent.setValue(true)
    }

    fun showBottomSheet() {
        _showBottomSheetEvent.setValue(true)
    }
}
